<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="/WEB-INF/jsp/layout/header.jsp" %>

<h2 class="mb-4">Аренда #${rental.id}</h2>

<div class="row justify-content-center">
    <div class="col-md-7">
        <div class="card shadow">
            <div class="card-body">
                <table class="table table-borderless">
                    <tr>
                        <th>Клиент:</th>
                        <td>${rental.clientName}</td>
                    </tr>
                    <tr>
                        <th>Машина:</th>
                        <td>${rental.carName} (${rental.carLicensePlate})</td>
                    </tr>
                    <tr>
                        <th>Дата начала:</th>
                        <td>${rental.startDate}</td>
                    </tr>
                    <tr>
                        <th>Дата окончания:</th>
                        <td>${rental.endDate}</td>
                    </tr>
                    <tr>
                        <th>Сумма:</th>
                        <td><strong>${rental.totalAmount} &#8381;</strong></td>
                    </tr>
                    <tr>
                        <th>Статус:</th>
                        <td>
                            <c:choose>
                                <c:when test="${rental.statusName == 'Активна'}">
                                    <span class="badge bg-success">${rental.statusName}</span>
                                </c:when>
                                <c:when test="${rental.statusName == 'Завершена'}">
                                    <span class="badge bg-secondary">${rental.statusName}</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-warning">${rental.statusName}</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </table>

                <div class="d-flex gap-2">
                    <a href="${pageContext.request.contextPath}/admin/rentals/${rental.id}/edit"
                       class="btn btn-warning">Редактировать</a>
                    <form action="${pageContext.request.contextPath}/admin/rentals/${rental.id}/delete"
                          method="post" onsubmit="return confirm('Удалить аренду?');">
                        <button type="submit" class="btn btn-danger">Удалить</button>
                    </form>
                    <a href="${pageContext.request.contextPath}/admin/rentals" class="btn btn-secondary">Назад к списку</a>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>
