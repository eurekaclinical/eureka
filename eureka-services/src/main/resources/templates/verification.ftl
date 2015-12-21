<#--
 #%L
 Eureka Services
 %%
 Copyright (C) 2012 - 2013 Emory University
 %%
 This program is dual licensed under the Apache 2 and GPLv3 licenses.
 
 Apache License, Version 2.0:
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
      http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 
 GNU General Public License version 3:
 
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as
 published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU General Public
 License along with this program.  If not, see
 <http://www.gnu.org/licenses/gpl-3.0.html>.
 #L%
-->
Dear ${user.firstName} ${user.lastName},

Welcome to Eureka! Clinical Analytics.

We received an account request using this email address. If it was indeed from you, please click this link to continue the account creation process:

${verificationUrl}${user.verificationCode}

If clicking the link does not work for some reason, you may copy and paste the link into any web browser. If you do not do this within ${config.registrationTimeout} hours, your account request will be deleted, and you will need to register again.

<#if (config.supportUri.name)?has_content>
This step is needed to ensure that your email address is not being misused by a third party. If you did not request an account on Eureka! Clinical Analytics, we apologize for the inconvenience. Please contact ${config.supportUri.name} to report the incident, or you may just ignore this message.
<#else>
This step is needed to ensure that your email address is not being misused by a third party. If you did not request an account on Eureka! Clinical Analytics, we apologize for the inconvenience. You may just ignore this message.
</#if>

Thanks,
The Eureka! Clinical Analytics Team
