<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="/WEB-INF/jsp/layout/header.jsp" %>

<h2 class="mb-4">Мой профиль</h2>

<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card shadow">
            <div class="card-body">
                <table class="table table-borderless">
                    <tr>
                        <th class="text-muted" style="width:40%">Имя</th>
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
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>
