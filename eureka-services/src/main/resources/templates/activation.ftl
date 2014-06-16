<#--
 #%L
 Eureka Services
 %%
 Copyright (C) 2012 - 2013 Emory University
 %%
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
      http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 #L%
-->
Dear ${user.firstName} ${user.lastName},

Your account on Eureka! Clinical Analytics has been activated. Go to ${applicationUrl} and click the Login button, and enter the email address and password that you provided when you registered for an account.
<#if (config.supportUri.name)?has_content>

If you did not request an account on Eureka! Clinical Analytics, we apologize for the inconvenience. Please contact us at ${config.supportUri.name} to report the incident as soon as possible.
</#if>

Thanks,
The Eureka! Clinical Analytics Team
