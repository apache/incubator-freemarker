<#--
  Licensed to the Apache Software Foundation (ASF) under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The ASF licenses this file
  to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
-->
<html>
<head>
<title>FreeMarker: NoParse Test</title>
</head>
<body>
<#noParse>

A simple test follows:

${message}

A more rigorous test, showing that we're not faking it:

${message@#$%&}

</#noParse>
<#if message?exists>
	<p>Message exists!
	<#noParse>
		...and even generates output!
		<#if message>
			Nested statements are ok, too.
		</#if>
	</#noParse>
	</p>
</#if>

Here's another edge case, this time, trying to output a &lt;noParse&gt;
inside another &lt;noParse&gt;

<#noParse>

This is what the noParse instruction looks like:

<#noP</#noParse><#noParse>arse>This part of the template wont be parsed by the
FreeMarker parser. Instead, it will be treated as verbatim text information,
and output as such.</#noP</#noParse><#noParse>arse>

The rest of the template appears here.
</#noParse>

Simple.
</body>
</html>
