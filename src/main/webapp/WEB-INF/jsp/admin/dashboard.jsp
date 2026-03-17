<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="/WEB-INF/jsp/layout/header.jsp" %>

<h2 class="mb-4">Панель администратора</h2>

<div class="row mb-4">
    <div class="col-md-4">
        <div class="card text-white bg-primary">
            <div class="card-body">
                <h5 class="card-title">Машины</h5>
                <h2>${stats.cars}</h2>
            </div>
        </div>
    </div>
    <div class="col-md-4">
        <div class="card text-white bg-success">
            <div class="card-body">
                <h5 class="card-title">Клиенты</h5>
                <h2>${stats.clients}</h2>
            </div>
        </div>
    </div>
    <div class="col-md-4">
        <div class="card text-white bg-info">
            <div class="card-body">
                <h5 class="card-title">Аренды</h5>
                <h2>${stats.rentals}</h2>
            </div>
        </div>
    </div>
</div>

<h3>Быстрые действия</h3>
<div class="list-group">
    <a href="${pageContext.request.contextPath}/admin/clients" class="list-group-item list-group-item-action">
        Управление клиентами
    </a>
    <a href="${pageContext.request.contextPath}/admin/rentals" class="list-group-item list-group-item-action">
        Управление арендами
    </a>
    <a href="${pageContext.request.contextPath}/admin/cars" class="list-group-item list-group-item-action">
        Управление машинами
    </a>
    <a href="${pageContext.request.contextPath}/admin/violation-types" class="list-group-item list-group-item-action">
        Управление типами нарушений
    </a>
    <a href="${pageContext.request.contextPath}/admin/statistics" class="list-group-item list-group-item-action">
        Статистика аренд
    </a>
</div>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>
