<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%@ include file="/WEB-INF/jsp/layout/header.jsp" %>

<h2 class="mb-4">Мои аренды</h2>

<c:choose>
    <c:when test="${empty rentals}">
        <div class="alert alert-info">
            У вас пока нет аренд.
            <a href="${pageContext.request.contextPath}/catalog">Перейти в каталог</a>
        </div>
    </c:when>
    <c:otherwise>
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Машина</th>
                        <th>Начало</th>
                        <th>Окончание</th>
                        <th>Сумма</th>
                        <th>Статус</th>
                        <th>Действия</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="rental" items="${rentals}">
                        <tr>
                            <td>${rental.id}</td>
                            <td>${rental.carName} (${rental.carLicensePlate})</td>
                            <td>${rental.startDate}</td>
                            <td>${rental.endDate}</td>
                            <td>${rental.totalAmount} &#8381;</td>
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
                            <td>
                                <a href="${pageContext.request.contextPath}/account/rentals/${rental.id}"
                                   class="btn btn-sm btn-outline-primary">Подробнее</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>
