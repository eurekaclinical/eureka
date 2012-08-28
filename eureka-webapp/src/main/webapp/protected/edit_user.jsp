<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template"%>
<%@ taglib uri="/WEB-INF/tlds/function.tld" prefix="myfn"%>



<template:insert template="/templates/eureka_main.jsp">
    
    
    <template:content name="sidebar">
            <img src="${pageContext.request.contextPath}/images/bioinformatics.jpg" />
    </template:content>

    <template:content name="content">

            <h3>Edit User</h3>
            <div class="pad pad_top">
                <form action="admin" method="GET" id="editUserForm">
                <table>
                    <tr>
                        <td width="124">Name:</td>
                        <td colspan="4">${user.firstName} ${user.lastName}
                                <input type="hidden" name="id" value="${user.id}" />
                                <input type="hidden" name="action" value="save" />
                        </td>
                    </tr>
                    <tr>
                        <td>Organization:</td>
                        <td colspan="4">${user.organization}</td>
                    </tr>
                    <tr>
                        <td>Email:</td>
                        <td colspan="4">${user.email}</td>
                    </tr>
                    <tr>
                        <td>Role:</td>
                        <td colspan="4">
                            <c:forEach var="role" items="${roles}">
                                <c:set var="hasRole" value="0"></c:set>
                                <c:set var="isSuperUser" value="0"></c:set>
                            
                                <c:forEach var="userRole" items="${user.roles}">
                                    
                                    <c:if test="${userRole.id ==  role.id}">
                                        <c:set var="hasRole" value="1"></c:set>
                                    </c:if>
                                    <c:if test="${userRole.name eq 'superuser'}">
                                        <c:set var="isSuperUser" value="1"></c:set>
                                    </c:if>
                                    
            
                                </c:forEach> 
                                <c:choose>
                                    <c:when test="${hasRole == 1}">
                                        <input type="checkbox" name="role" id="role" value="${role.id}" checked ${role.name eq  'superuser' ? "disabled" : ''}/>${role.name}<span class="status"></span><br />
                                    </c:when>
                                    <c:otherwise>
                                        <input type="checkbox" name="role" id="role" value="${role.id}" />${role.name}<span class="status"></span><br />
                                    
                                    </c:otherwise>
                                </c:choose>

                            </c:forEach>                        
                        </td>
                    </tr>
                    <tr>
                        <td>Activated</td>
                        <td colspan="4">
                                <c:choose>
                                    <c:when test="${user.active == true}">
                                        <input type="checkbox" name="active" id="active"  checked ${isSuperUser ==  1 ? "disabled" : ''}/><span class="status"></span><br />
                                    </c:when>
                                    <c:otherwise>
                                        <input type="checkbox" name="active" id="active" /><span class="status"></span><br />
                                    
                                    </c:otherwise>
                                </c:choose>
                        </td>
                    </tr>
                    
                    <tr>
                        <td>Verification Status:</td>
                        <td colspan="4">
                                <c:choose>
                                    <c:when test="${user.verified == true}">
                                        Verified<span class="status"></span><br />
                                    </c:when>
                                    <c:otherwise>
                                        Un-verified<span class="status"></span><br />                                   
                                    </c:otherwise>
                                </c:choose>
                        </td>
                    </tr>
                    <tr>
                      <td>&nbsp;</td>
                      <td colspan="4">
                                    <button class="btn btn-primary fltrt">
                    Save
                </button>
                </td>
                  </tr>

                </table>
        
            </form>

        </div>
    </template:content>
    <template:content name="subcontent">
        <div>
            <div id="release_notes">
                <h3>
                    <img src="${pageContext.request.contextPath}/images/rss.png"
                        border="0" /> Related News <a href="xml/rss_news.xml" class="rss"></a>
                </h3>
                <script language="JavaScript"
                    src="http://feed2js.org//feed2js.php?src=http%3A%2F%2Fwhsc.emory.edu%2Fhome%2Fnews%2Freleases%2Fresearch.rss&chan=y&num=4&desc=1&utf=y"
                    charset="UTF-8" type="text/javascript"></script>

                <noscript>
                    <a style="padding-left: 35px"
                        href="http://feed2js.org//feed2js.php?src=http%3A%2F%2Fwhsc.emory.edu%2Fhome%2Fnews%2Freleases%2Fresearch.rss&chan=y&num=4&desc=200>1&utf=y&html=y">View
                        RSS feed</a>
                </noscript>

            </div>
        </div>
    </template:content>

</template:insert>
