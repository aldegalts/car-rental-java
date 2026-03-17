<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="/WEB-INF/jsp/layout/header.jsp" %>

<h2 class="mb-4">
    <c:choose>
        <c:when test="${not empty violationType}">Редактирование типа нарушения</c:when>
        <c:otherwise>Новый тип нарушения</c:otherwise>
    </c:choose>
</h2>

<div class="row justify-content-center">
    <div class="col-md-6">
        <div class="card shadow">
            <div class="card-body">
                <c:set var="actionUrl" value="${pageContext.request.contextPath}/admin/violation-types/create" />
                <c:if test="${not empty violationType}">
                    <c:set var="actionUrl" value="${pageContext.request.contextPath}/admin/violation-types/${violationType.id}/edit" />
                </c:if>

                <form action="${actionUrl}" method="post">
                    <div class="mb-3">
                        <label for="typeName" class="form-label">Название *</label>
                        <input type="text" class="form-control" id="typeName" name="typeName"
                               value="${not empty typeName ? typeName : violationType.typeName}" required>
                    </div>

                    <div class="mb-3">
                        <label for="defaultFine" class="form-label">Штраф по умолчанию (₽) *</label>
                        <input type="number" class="form-control" id="defaultFine" name="defaultFine"
                               step="0.01" min="0"
                               value="${not empty defaultFine ? defaultFine : violationType.defaultFine}" required>
                    </div>

                    <div class="mb-3">
                        <label for="description" class="form-label">Описание</label>
                        <textarea class="form-control" id="description" name="description"
                                  rows="3">${not empty description ? description : violationType.description}</textarea>
                    </div>

                    <div class="d-flex gap-2">
                        <button type="submit" class="btn btn-primary">
                            <c:choose>
                                <c:when test="${not empty violationType}">Сохранить</c:when>
                                <c:otherwise>Создать</c:otherwise>
                            </c:choose>
                        </button>
                        <a href="${pageContext.request.contextPath}/admin/violation-types" class="btn btn-secondary">Отмена</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>
