package com.tiv.inventory.transfer.graph.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.tiv.inventory.transfer.constant.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * 格式化调拨建议节点
 */
@Slf4j
@AllArgsConstructor
public class ProcessSuggestFormatNode implements NodeAction {

    private final ChatClient client;

    private final static String SYSTEM_PROMPT = """
            # 角色:
                你是一个数据处理专家, 擅长从文本数据中提取JSON数据并且修正格式.
            # 输出要求:
                仅输出JSON格式数据, 禁止输出Markdown格式, 禁止改变原来的数据类型, 并且不要输出任何解释, 任何备注或者Markdown标记.
                输出中严格禁止包含 json 或者 ```json 或者 ``` 之类的字符, 也不要包含多余的文字.
            """;

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        // 1. 从状态机获取原始调拨建议
        String transferSuggestRawData = state.value(Constants.TRANSFER_SUGGEST_RAW_DATA, "");

        // 2. 调用大模型处理原始调拨建议成JSON格式
        Flux<String> flux = client.prompt()
                .system(SYSTEM_PROMPT)
                .user(t -> t.text("""
                        文本数据: {transferSuggestRawData}
                        """).param(Constants.TRANSFER_SUGGEST_RAW_DATA, transferSuggestRawData))
                .stream()
                .content();
        StringBuilder sb = new StringBuilder();
        flux.doOnNext(sb::append).blockLast();
        String formattedSuggest = sb.toString();
        log.info("格式化后的调拨建议: {}", formattedSuggest);

        if (formattedSuggest.contains("json") || formattedSuggest.contains("```")) {
            int start = formattedSuggest.indexOf('{');
            int end = formattedSuggest.lastIndexOf('}');
            formattedSuggest = formattedSuggest.substring(start, end + 1);
            log.info("二次格式化后的调拨建议: {}", formattedSuggest);
        }

        log.info("ProcessSuggestFormatNode--apply--success");
        return Map.of(Constants.TRANSFER_SUGGEST_FORMATTED_DATA, formattedSuggest);
    }

}
