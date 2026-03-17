<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="/WEB-INF/jsp/layout/header.jsp" %>

<h2 class="mb-4">Клиент #${client.id}</h2>

<c:if test="${param.message == 'updated'}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        Клиент успешно обновлён
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>

<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card shadow">
            <div class="card-body">
                <table class="table table-borderless">
                    <tr>
                        <th class="text-muted" style="width:40%">ID</th>
                        <td>${client.id}</td>
                    </tr>
                    <tr>
                        <th class="text-muted">Пользователь</th>
                        <td>${client.username}</td>
                    </tr>
                    <tr>
                        <th class="text-muted">Имя</th>
                        <td>${client.name}</td>
                    </tr>
                    <tr>
                        <th class="text-muted">Фамилия</th>
                        <td>${client.surname}</td>
                    </tr>
                    <tr>
                        <th class="text-muted">Дата рождения</th>
                        <td>${client.birthDate}</td>
                    </tr>
                    <tr>
                        <th class="text-muted">Телефон</th>
                        <td>${client.phone}</td>
                    </tr>
                    <tr>
                        <th class="text-muted">Email</th>
                        <td>${client.email}</td>
                    </tr>
                    <tr>
                        <th class="text-muted">Водительское удостоверение</th>
                        <td>${client.driverLicense}</td>
                    </tr>
                    <tr>
                        <th class="text-muted">Срок действия ВУ</th>
                        <td>${client.licenseExpiryDate}</td>
                    </tr>
                </table>

                <div class="d-flex gap-2">
                    <a href="${pageContext.request.contextPath}/admin/clients/${client.id}/edit"
                       class="btn btn-primary">
                        <i class="bi bi-pencil"></i> Редактировать
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/clients" class="btn btn-secondary">
                        <i class="bi bi-arrow-left"></i> К списку
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>
