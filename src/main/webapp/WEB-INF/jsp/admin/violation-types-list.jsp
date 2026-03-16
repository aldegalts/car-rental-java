<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="/WEB-INF/jsp/layout/header.jsp" %>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h2>Типы нарушений</h2>
    <a href="${pageContext.request.contextPath}/admin/violation-types/create" class="btn btn-primary">
        <i class="bi bi-plus-lg"></i> Добавить
    </a>
</div>

<c:if test="${param.message == 'created'}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        Тип нарушения успешно создан
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>
<c:if test="${param.message == 'updated'}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        Тип нарушения успешно обновлён
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>
<c:if test="${param.message == 'deleted'}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        Тип нарушения удалён
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>

<c:choose>
    <c:when test="${empty violationTypes}">
        <div class="alert alert-info">Типы нарушений не найдены</div>
    </c:when>
    <c:otherwise>
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Название</th>
                        <th>Штраф по умолчанию</th>
                        <th>Описание</th>
                        <th>Действия</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="vt" items="${violationTypes}">
                        <tr>
                            <td>${vt.id}</td>
                            <td>${vt.typeName}</td>
                            <td>${vt.defaultFine} ₽</td>
                            <td>${vt.description}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/violation-types/${vt.id}/edit"
                                   class="btn btn-sm btn-outline-primary">
                                    <i class="bi bi-pencil"></i> Изменить
                                </a>
                                <form action="${pageContext.request.contextPath}/admin/violation-types/${vt.id}/delete"
                                      method="post" class="d-inline"
                                      onsubmit="return confirm('Удалить тип нарушения?')">
                                    <button type="submit" class="btn btn-sm btn-outline-danger">
                                        <i class="bi bi-trash"></i> Удалить
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:otherwise>
</c:choose>

<a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-secondary mt-3">
    <i class="bi bi-arrow-left"></i> Назад к панели
</a>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>
