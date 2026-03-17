<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="/WEB-INF/jsp/layout/header.jsp" %>

<h2 class="mb-4">Оформление аренды</h2>

<div class="row">
    <div class="col-md-6">
        <div class="card shadow mb-4">
            <div class="card-header">
                <h5 class="mb-0">Информация о машине</h5>
            </div>
            <div class="card-body">
                <table class="table table-borderless mb-0">
                    <tr>
                        <th>Марка:</th>
                        <td>${car.brand}</td>
                    </tr>
                    <tr>
                        <th>Модель:</th>
                        <td>${car.model}</td>
                    </tr>
                    <tr>
                        <th>Год:</th>
                        <td>${car.year}</td>
                    </tr>
                    <tr>
                        <th>Гос. номер:</th>
                        <td>${car.licensePlate}</td>
                    </tr>
                    <tr>
                        <th>Стоимость/день:</th>
                        <td><strong>${car.dailyCost} &#8381;</strong></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>

    <div class="col-md-6">
        <div class="card shadow">
            <div class="card-header">
                <h5 class="mb-0">Параметры аренды</h5>
            </div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/rentals/create/${car.id}" method="post">
                    <div class="mb-3">
                        <label class="form-label">Дата начала</label>
                        <input type="datetime-local" class="form-control" disabled
                               value="<%= java.time.LocalDateTime.now().withSecond(0).withNano(0) %>">
                    </div>

                    <div class="mb-3">
                        <label for="endDate" class="form-label">Дата окончания *</label>
                        <input type="datetime-local" class="form-control" id="endDate" name="endDate" required>
                    </div>

                    <div class="d-flex gap-2">
                        <button type="submit" class="btn btn-success">Оформить аренду</button>
                        <a href="${pageContext.request.contextPath}/catalog/${car.id}" class="btn btn-secondary">Отмена</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>
