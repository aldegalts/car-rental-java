<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="/WEB-INF/jsp/layout/header.jsp" %>

<h2 class="mb-4">Клиенты</h2>

<c:if test="${param.message == 'updated'}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        Клиент успешно обновлён
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>

<c:choose>
    <c:when test="${empty clients}">
        <div class="alert alert-info">Клиенты не найдены</div>
    </c:when>
    <c:otherwise>
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Имя</th>
                        <th>Фамилия</th>
                        <th>Email</th>
                        <th>Телефон</th>
                        <th>Пользователь</th>
                        <th>Действия</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="client" items="${clients}">
                        <tr>
                            <td>${client.id}</td>
                            <td>${client.name}</td>
                            <td>${client.surname}</td>
                            <td>${client.email}</td>
                            <td>${client.phone}</td>
                            <td>${client.username}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/clients/${client.id}"
                                   class="btn btn-sm btn-outline-info" title="Просмотр">
                                    <i class="bi bi-eye"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/admin/clients/${client.id}/edit"
                                   class="btn btn-sm btn-outline-primary" title="Редактировать">
                                    <i class="bi bi-pencil"></i>
                                </a>
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
