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

import java.util.logging.Logger;

import org.osgi.framework.Bundle;


/**
 * @version $Revision: $ $Date: $
 */
public class ShareProxy implements Share
{
    private final static String CLASS_NAME = ShareProxy.class.getName();
    private final static Logger LOGGER = Logger.getLogger(CLASS_NAME);
    private final ShareImpl share;
    private final Bundle bundle;

    ShareProxy(ShareImpl share, Bundle bundle)
    {
        assert share != null;
        assert bundle != null;

        this.share = share;
        this.bundle = bundle;
    }

    public Object get(String key)
    {
        LOGGER.entering(CLASS_NAME, "get", key);

        if (key == null) throw new IllegalArgumentException("Key cannot be null");

        Object value = share.get(key);

        LOGGER.exiting(CLASS_NAME, "get", value);

        return value;
    }

    public void put(String key, Object value)
    {
        LOGGER.entering(CLASS_NAME, "get", new Object[]{key, value});

        if (key == null) throw new IllegalArgumentException("Key cannot be null");

        share.put(bundle, key, value);

        LOGGER.exiting(CLASS_NAME, "get");
    }

    public void clear()
    {
        LOGGER.entering(CLASS_NAME, "clear");

        share.clear(bundle);

        LOGGER.exiting(CLASS_NAME, "clear");
    }

    public void addListener(ShareListener listener)
    {
        LOGGER.entering(CLASS_NAME, "addListener", listener);

        if (listener == null) throw new IllegalArgumentException("Listener cannot be null");

        share.addListener(bundle, listener);

        LOGGER.exiting(CLASS_NAME, "addListener");
    }

    public void removeListener(ShareListener listener)
    {
        LOGGER.entering(CLASS_NAME, "removeListener", listener);

        if (listener == null) throw new IllegalArgumentException("Listener cannot be null");

        share.removeListener(bundle, listener);

        LOGGER.exiting(CLASS_NAME, "removeListener");
    }
}
