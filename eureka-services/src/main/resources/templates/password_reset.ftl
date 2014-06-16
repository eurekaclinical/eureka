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

<#if (config.supportUri.name)?has_content>
This is notification that your Eureka! Clinical Analytics password was reset. If you did not change your password on the website or make a support request to change your password, please contact us as soon as possible at ${config.supportUri.name}.
<#else>
This is notification that your Eureka! Clinical Analytics password was reset.
</#if>

Your new password is ${plainTextPassword}. You will be required to change your password when you login.

Thanks,
The Eureka! Clinical Analytics Team
