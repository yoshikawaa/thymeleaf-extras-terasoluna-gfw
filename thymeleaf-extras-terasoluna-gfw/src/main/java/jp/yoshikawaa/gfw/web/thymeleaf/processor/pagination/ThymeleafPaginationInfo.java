package jp.yoshikawaa.gfw.web.thymeleaf.processor.pagination;

import java.util.Map.Entry;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.terasoluna.gfw.web.pagination.PaginationInfo;
import org.thymeleaf.Arguments;

import jp.yoshikawaa.gfw.web.thymeleaf.util.ExpressionUtils;

public class ThymeleafPaginationInfo extends PaginationInfo {

    Arguments arguments;
    String expression;

    private int pageSize;
    private Sort pageSort;

    public ThymeleafPaginationInfo(Arguments arguments, Page<?> page, String expression, int maxDisplayCount) {
        this(arguments, page, expression, null, false, maxDisplayCount);
    }

    public ThymeleafPaginationInfo(Arguments arguments, Page<?> page, String expression, String criteriaQuery,
            boolean disableHtmlEscapeOfCriteriaQuery, int maxDisplayCount) {
        super(page, PaginationInfo.DEFAULT_PATH_TEMPLATE, PaginationInfo.DEFAULT_QUERY_TEMPLATE, criteriaQuery,
                disableHtmlEscapeOfCriteriaQuery, maxDisplayCount);

        this.arguments = arguments;
        this.pageSize = page.getSize();
        this.pageSort = page.getSort();
        this.expression = expression;
    }

    public String getPageUrl(int pageIndex) {
        if (StringUtils.hasText(expression)) {
            return getPageUrlUsingExpression(pageIndex);
        }

        return super.getPageUrl(pageIndex);
    }

    private String getPageUrlUsingExpression(int pageIndex) {

        StringBuilder pageUrlBuilder = new StringBuilder(expression);

        // resolve variables
        for (Entry<String, Object> a : createAttributeMap(pageIndex, pageSize, pageSort).entrySet()) {
            String replacementKey = "${" + a.getKey() + "}";
            int point = pageUrlBuilder.indexOf(replacementKey);
            if (point >= 0) {
                pageUrlBuilder.replace(point, point + replacementKey.length(), "'" + a.getValue() + "'");
            }
        }

        // resolve expression
        String pageUrl = ExpressionUtils.execute(arguments, pageUrlBuilder.toString(), String.class);

        // append criteria query
        String criteriaQuery = getCriteriaQuery();
        if (StringUtils.hasLength(criteriaQuery)) {
            return (expression.contains("?")) ? pageUrl + "&" + criteriaQuery : pageUrl + "?" + criteriaQuery;
        }
        return pageUrl;
    }

}
