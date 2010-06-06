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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
    private final Map<Bundle, List<ShareListener>> listeners = new HashMap<Bundle, List<ShareListener>>();


    Object get(String key)
    {
        LOGGER.entering(CLASS_NAME, "get", key);

        assert key != null;

        Object value;
        synchronized (lock)
        {
            value = map.get(key);
        }

        for (ShareListener listener : collectListeners()) listener.get(key, value);

        LOGGER.exiting(CLASS_NAME, "get", value);

        return value;
    }

    void put(Bundle bundle, String key, Object value)
    {
        LOGGER.entering(CLASS_NAME, "put", new Object[]{bundle, key, value});

        assert bundle != null;
        assert key != null;

        synchronized (lock)
        {
            if (!bundles.contains(bundle)) return;

            map.put(key, value);
            bundleKeys.put(key, bundle);
        }

        for (ShareListener listener : collectListeners()) listener.put(key, value);

        LOGGER.exiting(CLASS_NAME, "put");
    }

    void clear(Bundle bundle)
    {
        LOGGER.entering(CLASS_NAME, "clear", bundle);

        assert bundle != null;

        synchronized (lock)
        {
            if (!bundles.contains(bundle)) return;

            map.clear();
            bundleKeys.clear();
        }

        for (ShareListener listener : collectListeners()) listener.clear();

        LOGGER.exiting(CLASS_NAME, "clear");
    }


    void addListener(Bundle bundle, ShareListener listener)
    {
        LOGGER.entering(CLASS_NAME, "addListener", new Object[]{bundle, listener});

        assert bundle != null;

        synchronized (lock)
        {
            if (!bundles.contains(bundle)) return;

            List<ShareListener> list = listeners.get(bundle);
            if (list == null) listeners.put(bundle, list = new ArrayList<ShareListener>(1));
            list.add(listener);
        }

        LOGGER.exiting(CLASS_NAME, "addListener");
    }

    void removeListener(Bundle bundle, ShareListener listener)
    {
        LOGGER.entering(CLASS_NAME, "removeListener", new Object[]{bundle, listener});

        assert bundle != null;

        synchronized (lock)
        {
            if (!bundles.contains(bundle)) return;

            List<ShareListener> list = listeners.get(bundle);
            if (list == null) return;
            list.remove(listener);
            if (list.isEmpty()) listeners.remove(bundle);
        }

        LOGGER.exiting(CLASS_NAME, "removeListener");
    }

    public Object getService(Bundle bundle, ServiceRegistration registration)
    {
        LOGGER.entering(CLASS_NAME, "getService", new Object[]{bundle, registration});

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
        LOGGER.entering(CLASS_NAME, "ungetService", new Object[]{bundle, registration, service});

        assert bundle != null;
        assert service != null;

        synchronized (lock)
        {
            bundles.remove(bundle);
            listeners.remove(bundle);

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

    private List<ShareListener> collectListeners()
    {
        LOGGER.entering(CLASS_NAME, "collectListeners");

        List<ShareListener> list = new ArrayList<ShareListener>();

        for (List<ShareListener> l : listeners.values()) list.addAll(l);

        LOGGER.exiting(CLASS_NAME, "collectListeners", list);

        return list;
    }
}
