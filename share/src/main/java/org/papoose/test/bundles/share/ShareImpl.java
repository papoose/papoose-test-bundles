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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;


/**
 * @version $Revision: $ $Date: $
 */
public class ShareImpl implements ServiceFactory
{
    private final static String CLASS_NAME = ShareImpl.class.getName();
    private final static Logger LOGGER = Logger.getLogger(CLASS_NAME);
    private final Object lock = new Object();
    private final Map<String, Object> map = new HashMap<String, Object>();
    private final Set<Bundle> bundles = new HashSet<Bundle>();
    private final Map<String, Bundle> bundleKeys = new HashMap<String, Bundle>();


    Object get(String key)
    {
        LOGGER.entering(CLASS_NAME, "get", key);

        assert key != null;

        synchronized (lock)
        {
            Object value = map.get(key);

            LOGGER.exiting(CLASS_NAME, "get", value);

            return value;
        }
    }

    void put(Bundle bundle, String key, Object value)
    {
        LOGGER.entering(CLASS_NAME, "put", new Object[]{ bundle, key, value });

        assert bundle != null;
        assert key != null;

        synchronized (lock)
        {
            if (!bundles.contains(bundle)) return;

            map.put(key, value);
            bundleKeys.put(key, bundle);
        }

        LOGGER.exiting(CLASS_NAME, "put");
    }

    public Object getService(Bundle bundle, ServiceRegistration registration)
    {
        LOGGER.entering(CLASS_NAME, "getService", new Object[]{ bundle, registration });

        assert bundle != null;

        synchronized (lock)
        {
            bundles.add(bundle);
            Object service = new ShareProxy(this, bundle);

            LOGGER.exiting(CLASS_NAME, "getService", service);

            return service;
        }
    }

    public void ungetService(Bundle bundle, ServiceRegistration registration, Object service)
    {
        LOGGER.entering(CLASS_NAME, "ungetService", new Object[]{ bundle, registration, service });

        assert bundle != null;
        assert service != null;

        synchronized (lock)
        {
            bundles.remove(bundle);

            Set<String> keys = new HashSet<String>();
            for (Map.Entry<String, Bundle> entry : bundleKeys.entrySet())
            {
                if (bundle == entry.getValue()) keys.add(entry.getKey());
            }

            for (String key : keys)
            {
                bundleKeys.remove(key);
                map.remove(key);
            }
        }

        LOGGER.exiting(CLASS_NAME, "ungetService");
    }
}
