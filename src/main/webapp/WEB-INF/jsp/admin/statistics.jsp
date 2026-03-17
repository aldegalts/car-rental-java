<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="/WEB-INF/jsp/layout/header.jsp" %>

<h2 class="mb-4">Статистика аренд</h2>

<div class="card mb-4">
    <div class="card-body">
        <h5>Период</h5>
        <form method="get" action="${pageContext.request.contextPath}/admin/statistics" class="row g-3">
            <div class="col-md-4">
                <label for="startDate" class="form-label">Дата начала</label>
                <input type="datetime-local" class="form-control" id="startDate" name="startDate"
                       value="${startDate}" required>
            </div>
            <div class="col-md-4">
                <label for="endDate" class="form-label">Дата окончания</label>
                <input type="datetime-local" class="form-control" id="endDate" name="endDate"
                       value="${endDate}" required>
            </div>
            <div class="col-md-4">
                <label class="form-label">&nbsp;</label>
                <div>
                    <button type="submit" class="btn btn-primary">Получить статистику</button>
                </div>
            </div>
        </form>
    </div>
</div>

<c:if test="${not empty stats}">
    <div class="row mb-4">
        <div class="col-md-4">
            <div class="card text-white bg-primary">
                <div class="card-body">
                    <h5 class="card-title">Всего аренд</h5>
                    <h2>${stats.totalRentals}</h2>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card text-white bg-success">
                <div class="card-body">
                    <h5 class="card-title">Без нарушений</h5>
                    <h2>${stats.percentWithoutViolations}%</h2>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card text-white bg-danger">
                <div class="card-body">
                    <h5 class="card-title">С нарушениями</h5>
                    <h2>${stats.percentWithViolations}%</h2>
                </div>
            </div>
        </div>
    </div>

    <c:if test="${not empty stats.rentals}">
        <h3>Список аренд</h3>
        <div class="table-responsive">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Дата начала</th>
                        <th>Дата окончания</th>
                        <th>Сумма</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="rental" items="${stats.rentals}">
                        <tr>
                            <td>${rental.id}</td>
                            <td>${rental.startDate}</td>
                            <td>${rental.endDate}</td>
                            <td>${rental.totalAmount} &#8381;</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>
</c:if>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>
