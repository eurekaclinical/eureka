<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template"%>


<template:insert template="/templates/eureka_editor.jsp">

	<template:content name="sidebar">
		<img
			src="${pageContext.request.contextPath}/images/bioinformatics.jpg" />
	</template:content>

	<template:content name="content">

			<table align="center" border="0" cellpadding="0" cellspacing="0">
<tr><td> 
<!-- Tabs -->
        <div id="dialog" title="Create Workflow">

        </div>
  		<div id="wizard" class="swMain">
  			<ul id="steps">
  				<li><a href="#step-1">
                <label class="stepNumber">1</label>
                <span class="stepDesc">
                   Create Element<br />
                   <small>Create a New Derived Element or Workflow</small>
                </span>
            </a></li>
  				<li><a href="#step-2">
                <label class="stepNumber">2</label>
                <span class="stepDesc">
                   Select Type<br />
                   <small>Select Type of Element to create</small>
                </span>
            </a></li>
  				<li><a href="#step-3">
                <label class="stepNumber">3</label>
                <span class="stepDesc">
                   Select Elements<br />
                   <small>Select Elements from Ontology</small>
                </span>
            </a></li>
  				<li><a href="#step-4">
                <label class="stepNumber">4</label>
                <span class="stepDesc">
                   View New Element<br />
                   <small>Visualize Element</small>
                </span>                   
             </a></li>
  				<li><a href="#step-5">
                <label class="stepNumber">5</label>
                <span class="stepDesc">
                   Save<br />
                   <small>Save Element to Database</small>
                </span>                   
            </a></li>
  			</ul>
  			<div id="step-1">	
            	<h2 class="StepTitle">Create a New Derived Element</h2>
				<label>Enter Name for New Derived Element:</label><input type="text" id="element_name" width="100"/>
        	</div>
  			<div id="step-2">	
            	<h2 class="StepTitle">Select Type of Element to create</h2>
                    <p><br/></p>
                    <center>
                    <p>
                    <select id="type">
                        <option value="">--SELECT--</option>
                        <option value="OR">Category</option>
                        <option value="AND">Temporal</option>
                       

                    </select>
                    </p>
                    <p id="type_description"></p>
                    </center>
        	</div>
  			<div id="step-3">
            <h2 class="StepTitle">Select Elements from Ontology Explorer</h2>	
            <div>
                <div style="float: left">
                <p>
                    Drag and Drop an element from the System or User-Defined element explorer to the canvas.
                </p>
                </div>
                <div style="float: right; padding-right:5px" id="expand_div">
                    <a href="javascript:void(0)" alt="Expand Screen" id="expand"><img src="${pageContext.request.contextPath}/images/fullscreen.png" /></a>

                </div>
                <div style="float: right; padding-right:5px" id="collapse_div">
                    <a href="javascript:void(0)" alt="Expand Screen" id="collapse"><img src="${pageContext.request.contextPath}/images/fullscreen_exit.png" /></a>

                </div>

            </div>
            
            			<table style="width: 650px; " id="element_tree_table">
            				<tr>
            					<td  valign="top" width="25%">
		            				 <div class="tabs" style="float: left;" >
										        <ul class="tabNavigation">
										            <li><a href="#first">System</a></li>
										            <li><a href="#second">User Defined</a></li>
										        </ul>
										        <div id="first">
                                                    <div id="systemTree"></div>
										        </div>
										        <div id="second">
                                                    <div id="tab2"></div>
										        </div>							        
								    </div>
										
            					</td>
            					
            					<td valign="top">
            					
            						<table>
            							<tr>
            								<td colspan="2">
            									<h3 style="width: 300px"></h3>
           								</td>
            							</tr>
            							<tr>
            								<td>
			            						<table>
			            							<tr>
			            								<td>
							            					<div id="tree-drop" 
																class="jstree-drop"
																style="float: left; 																				
																border: 3px solid gray; 
																background: #C3D9FF; color: green;"
																>
																
																<ul id="sortable" style="width: 180px; height: 150px" >
									
																</ul>
                                                                <div id="holder" style="width: 550px; height: 350px" ></div>
																
																
															</div>
			            								
			            								</td>
			            							</tr>
            									</table>
            								
            								</td>
            							</tr>
            						</table>
            					</td>
							</tr>            			
            			</table>
								   
         
        </div>                      
  		<div id="step-4">
            <h2 class="StepTitle">Visualize New Element</h2>	
           	<div id="holder"></div>             				          
        </div>
  		<div id="step-5">
            <h2 class="StepTitle">Save Element to Database</h2>	
            <p>
            	Save the new element to the Database as a User Defined Element.
            </p>
        </div>
  		</div>
  		
<!-- End SmartWizard Content -->  		
  		
</td></tr>
</table>   

		</div>
	</template:content>
	<template:content name="subcontent">
		

			</div>
		</div>
	</template:content>

</template:insert>







			

			
			<!-- 
				<h2>Create New Derived Element</h2>

				<table>
					<tbody>
						<tr>
							<td>
								Element Search: <input type="text"></input>
							</td>
						</tr>
						<tr>
							<td valign="top">
								    <div class="tabs">
								        <ul class="tabNavigation">
								            <li><a href="#first">System</a></li>
								            <li><a href="#second">User Defined</a></li>
								        </ul>
								        <div id="first">
									        <div id="tab1" >
												<ul>
													<li id="phtml_1"><a href="#">System node 1</a>
														<ul>
															<li id="phtml_2"><a href="#">SysChild node 1</a></li>
															<li id="phtml_3"><a href="#">SysChild node 2</a></li>
														</ul></li>
													<li id="phtml_4"><a href="#">System node 2</a></li>
												</ul>
											</div>
								        </div>
								        <div id="second">
									        <div id="tab2">
												<ul>
													<li id="phtml_1"><a href="#">User node 1</a>
														<ul>
															<li id="phtml_2"><a href="#">UserChild node 1</a></li>
															<li id="phtml_3"><a href="#">UserChild node 2</a></li>
														</ul></li>
													<li id="phtml_4"><a href="#">User node 2</a></li>
												</ul>
											</div>
								        
								        </div>							        
							    	</div>
							
							</td>
							<td>
							<div id="tree-drop" 
								class="jstree-drop"
								style="float: left; 
								width: 950px;
								clear: both; 
								border: 3px solid gray; 
								background: #C3D9FF; color: green; 
								height: 480px; line-height: 20px; 
								text-align: center; font-size: 14px;">
								
									<ul id="sortable">
		
									</ul>
							
								<div id="holder"></div>
							</div>
							
							</td>
						</tr>
					</tbody>
				</table>
				 -->
				

	

						
