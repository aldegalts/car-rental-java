<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ include file="/WEB-INF/jsp/layout/header.jsp" %>

<div class="text-center mt-5">
    <h1 class="display-1">404</h1>
    <p class="lead">Страница не найдена</p>
    <a href="${pageContext.request.contextPath}/catalog" class="btn btn-primary">На главную</a>
</div>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>
