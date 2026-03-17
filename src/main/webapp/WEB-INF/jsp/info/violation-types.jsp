<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="/WEB-INF/jsp/layout/header.jsp" %>

<h2 class="mb-4">Типы нарушений</h2>

<c:choose>
    <c:when test="${not empty violationTypes}">
        <c:forEach var="type" items="${violationTypes}">
            <div class="card mb-3">
                <div class="card-body">
                    <h5 class="card-title">${type.typeName}</h5>
                    <p class="mb-2"><strong>Штраф:</strong> ${type.defaultFine} &#8381;</p>
                    <c:if test="${not empty type.description}">
                        <p class="card-text mb-0">${type.description}</p>
                    </c:if>
                </div>
            </div>
        </c:forEach>
    </c:when>
    <c:otherwise>
        <div class="alert alert-info">
            <p class="mb-0">Типы нарушений не найдены.</p>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>
