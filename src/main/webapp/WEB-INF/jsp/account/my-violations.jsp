<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="/WEB-INF/jsp/layout/header.jsp" %>

<h2 class="mb-4">Мои нарушения</h2>

<c:choose>
    <c:when test="${empty violations}">
        <div class="alert alert-info">У вас нет нарушений.</div>
    </c:when>
    <c:otherwise>
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Тип</th>
                        <th>Описание</th>
                        <th>Штраф</th>
                        <th>Дата</th>
                        <th>Оплата</th>
                        <th>Действия</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="v" items="${violations}">
                        <tr>
                            <td>${v.id}</td>
                            <td>${v.violationTypeName}</td>
                            <td>${v.description}</td>
                            <td>${v.fineAmount} &#8381;</td>
                            <td>${v.violationDate}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${v.paid}">
                                        <span class="badge bg-success">Оплачено</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-danger">Не оплачено</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/account/violations/${v.id}"
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
