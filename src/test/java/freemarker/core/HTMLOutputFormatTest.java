/*
 * Copyright 2014 Attila Szegedi, Daniel Dekany, Jonathan Revusky
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package freemarker.core;

import static freemarker.core.HTMLOutputFormat.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

import freemarker.template.TemplateModelException; 

public class HTMLOutputFormatTest {
    
    @Test
    public void testOutputMO() throws TemplateModelException, IOException {
       StringWriter out = new StringWriter();
       
       INSTANCE.output(INSTANCE.fromMarkup("<p>Test "), out);
       INSTANCE.output(INSTANCE.fromPlainTextByEscaping("foo & bar "), out);
       INSTANCE.output(INSTANCE.fromPlainTextByEscaping("baaz "), out);
       INSTANCE.output(INSTANCE.fromPlainTextByEscaping("<b>A</b> <b>B</b> <b>C</b>"), out);
       INSTANCE.output(INSTANCE.fromPlainTextByEscaping(""), out);
       INSTANCE.output(INSTANCE.fromPlainTextByEscaping("\"' x's \"y\" \""), out);
       INSTANCE.output(INSTANCE.fromMarkup("</p>"), out);
       
       assertEquals(
               "<p>Test "
               + "foo &amp; bar "
               + "baaz "
               + "&lt;b&gt;A&lt;/b&gt; &lt;b&gt;B&lt;/b&gt; &lt;b&gt;C&lt;/b&gt;"
               + "&quot;&#39; x&#39;s &quot;y&quot; &quot;"
               + "</p>",
               out.toString());
    }
    
    @Test
    public void testOutputString() throws TemplateModelException, IOException {
        StringWriter out = new StringWriter();
        
        INSTANCE.output("a", out);
        INSTANCE.output("<", out);
        INSTANCE.output("b'c", out);
        
        assertEquals("a&lt;b&#39;c", out.toString());
    }
    
    @Test
    public void testEscapePlainText() throws TemplateModelException {
        String plainText = "a&b";
        TemplateHTMLOutputModel mo = INSTANCE.fromPlainTextByEscaping(plainText);
        assertSame(plainText, mo.getPlainTextContent());
        assertNull(mo.getMarkupContent()); // Not the MO's duty to calculate it!
    }

    @Test
    public void testFromMarkup() throws TemplateModelException {
        String markup = "a&amp;b";
        TemplateHTMLOutputModel mo = INSTANCE.fromMarkup(markup);
        assertSame(markup, mo.getMarkupContent());
        assertNull(mo.getPlainTextContent()); // Not the MO's duty to calculate it!
    }
    
    @Test
    public void testMarkup() throws TemplateModelException {
        {
            String markup = "a&amp;b";
            TemplateHTMLOutputModel mo = INSTANCE.fromMarkup(markup);
            assertSame(markup, INSTANCE.getMarkup(mo));
        }
        
        {
            String safe = "abc";
            TemplateHTMLOutputModel mo = INSTANCE.fromPlainTextByEscaping(safe);
            assertSame(safe, INSTANCE.getMarkup(mo));
        }
        {
            String safe = "";
            TemplateHTMLOutputModel mo = INSTANCE.fromPlainTextByEscaping(safe);
            assertSame(safe, INSTANCE.getMarkup(mo));
        }
        {
            TemplateHTMLOutputModel mo = INSTANCE.fromPlainTextByEscaping("<abc");
            assertEquals("&lt;abc", INSTANCE.getMarkup(mo));
        }
        {
            TemplateHTMLOutputModel mo = INSTANCE.fromPlainTextByEscaping("abc>");
            assertEquals("abc&gt;", INSTANCE.getMarkup(mo));
        }
        {
            TemplateHTMLOutputModel mo = INSTANCE.fromPlainTextByEscaping("<abc>");
            assertEquals("&lt;abc&gt;", INSTANCE.getMarkup(mo));
        }
        {
            TemplateHTMLOutputModel mo = INSTANCE.fromPlainTextByEscaping("a&bc");
            assertEquals("a&amp;bc", INSTANCE.getMarkup(mo));
        }
        {
            TemplateHTMLOutputModel mo = INSTANCE.fromPlainTextByEscaping("a&b&c");
            assertEquals("a&amp;b&amp;c", INSTANCE.getMarkup(mo));
        }
        {
            TemplateHTMLOutputModel mo = INSTANCE.fromPlainTextByEscaping("a<&>b&c");
            assertEquals("a&lt;&amp;&gt;b&amp;c", INSTANCE.getMarkup(mo));
        }
        {
            TemplateHTMLOutputModel mo = INSTANCE.fromPlainTextByEscaping("\"<a<&>b&c>\"");
            assertEquals("&quot;&lt;a&lt;&amp;&gt;b&amp;c&gt;&quot;", INSTANCE.getMarkup(mo));
        }
        {
            TemplateHTMLOutputModel mo = INSTANCE.fromPlainTextByEscaping("<");
            assertEquals("&lt;", INSTANCE.getMarkup(mo));
        }
        {
            TemplateHTMLOutputModel mo = INSTANCE.fromPlainTextByEscaping("'");
            String mc = INSTANCE.getMarkup(mo);
            assertEquals("&#39;", mc);
            assertSame(mc, INSTANCE.getMarkup(mo)); // cached
        }
    }
    
    @Test
    public void testConcat() throws Exception {
        assertMO(
                "ab", null,
                INSTANCE.concat(new TemplateHTMLOutputModel("a", null), new TemplateHTMLOutputModel("b", null)));
        assertMO(
                null, "ab",
                INSTANCE.concat(new TemplateHTMLOutputModel(null, "a"), new TemplateHTMLOutputModel(null, "b")));
        assertMO(
                null, "<a>&lt;b&gt;",
                INSTANCE.concat(new TemplateHTMLOutputModel(null, "<a>"), new TemplateHTMLOutputModel("<b>", null)));
        assertMO(
                null, "&lt;a&gt;<b>",
                INSTANCE.concat(new TemplateHTMLOutputModel("<a>", null), new TemplateHTMLOutputModel(null, "<b>")));
    }
    
    private void assertMO(String pc, String mc, TemplateHTMLOutputModel mo) {
        assertEquals(pc, mo.getPlainTextContent());
        assertEquals(mc, mo.getMarkupContent());
    }
    
    @Test
    public void testGetMimeType() {
        assertEquals("text/html", INSTANCE.getMimeType());
    }
    
}
