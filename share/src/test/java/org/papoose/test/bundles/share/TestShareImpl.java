/**
 *
 * Copyright 2010 (C) The original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.papoose.test.bundles.share;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import org.osgi.framework.Bundle;


/**
 * @version $Revision: $ $Date: $
 */
public class TestShareImpl
{
    @Test
    public void test() throws Exception
    {
        Bundle one = mock(Bundle.class);
        Bundle two = mock(Bundle.class);

        ShareImpl factory = new ShareImpl();

        Share shareOne = (Share) factory.getService(one, null);
        Share shareTwo = (Share) factory.getService(two, null);

        shareOne.put("ONE", "ONE_VALUE");
        shareTwo.put("TWO", "TWO_VALUE");

        assertEquals("ONE_VALUE", shareOne.get("ONE"));
        assertEquals("ONE_VALUE", shareTwo.get("ONE"));
        assertEquals("TWO_VALUE", shareOne.get("TWO"));
        assertEquals("TWO_VALUE", shareTwo.get("TWO"));

        factory.ungetService(one, null, shareOne);

        assertNull(shareOne.get("ONE"));
        assertNull(shareTwo.get("ONE"));
        assertEquals("TWO_VALUE", shareOne.get("TWO"));
        assertEquals("TWO_VALUE", shareTwo.get("TWO"));

        shareOne.put("ONE", "ONE_VALUE");

        assertNull(shareOne.get("ONE"));
        assertNull(shareTwo.get("ONE"));
        assertEquals("TWO_VALUE", shareOne.get("TWO"));
        assertEquals("TWO_VALUE", shareTwo.get("TWO"));

        shareTwo.put("ONE", "ONE_VALUE");

        assertEquals("ONE_VALUE", shareOne.get("ONE"));
        assertEquals("ONE_VALUE", shareTwo.get("ONE"));
        assertEquals("TWO_VALUE", shareOne.get("TWO"));
        assertEquals("TWO_VALUE", shareTwo.get("TWO"));

        factory.ungetService(two, null, shareTwo);

        assertNull(shareOne.get("ONE"));
        assertNull(shareTwo.get("ONE"));
        assertNull(shareOne.get("TWO"));
        assertNull(shareTwo.get("TWO"));
    }
}
