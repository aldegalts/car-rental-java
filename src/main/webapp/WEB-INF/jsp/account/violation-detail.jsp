<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="/WEB-INF/jsp/layout/header.jsp" %>

<h2 class="mb-4">Нарушение #${violation.id}</h2>

<div class="row justify-content-center">
    <div class="col-md-7">
        <div class="card shadow">
            <div class="card-body">
                <table class="table table-borderless">
                    <tr>
                        <th>Тип нарушения:</th>
                        <td>${violation.violationTypeName}</td>
                    </tr>
                    <tr>
                        <th>Описание:</th>
                        <td>${violation.description}</td>
                    </tr>
                    <tr>
                        <th>Сумма штрафа:</th>
                        <td><strong>${violation.fineAmount} &#8381;</strong></td>
                    </tr>
                    <tr>
                        <th>Дата нарушения:</th>
                        <td>${violation.violationDate}</td>
                    </tr>
                    <tr>
                        <th>Статус оплаты:</th>
                        <td>
                            <c:choose>
                                <c:when test="${violation.paid}">
                                    <span class="badge bg-success">Оплачено</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-danger">Не оплачено</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <th>Аренда:</th>
                        <td>
                            <a href="${pageContext.request.contextPath}/account/rentals/${violation.rentalId}">
                                Аренда #${violation.rentalId}
                            </a>
                        </td>
                    </tr>
                </table>

                <a href="${pageContext.request.contextPath}/account/violations" class="btn btn-secondary">Назад к списку</a>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>
