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

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;


/**
 * @version $Revision: $ $Date: $
 */
public class Activator implements BundleActivator
{
    private final static String CLASS_NAME = Activator.class.getName();
    private final static Logger LOGGER = Logger.getLogger(CLASS_NAME);
    private final ShareImpl factory = new ShareImpl();
    private volatile ServiceRegistration registration;

    public void start(BundleContext context) throws Exception
    {
        LOGGER.entering(CLASS_NAME, "start", context);

        registration = context.registerService(Share.class.getName(), factory, null);

        LOGGER.exiting(CLASS_NAME, "start");
    }

    public void stop(BundleContext context) throws Exception
    {
        LOGGER.entering(CLASS_NAME, "stop", context);

        registration.unregister();
        registration = null;

        LOGGER.exiting(CLASS_NAME, "stop");
    }
}
