/**
 * Copyright 2013-2015 PayPal
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
package com.paypal.cascade.common.properties

import java.io.IOException
import java.util.Properties

import com.paypal.cascade.common.logging.LoggingSugar

/**
 * Class specifically for accessing values from build.properties.
 *
 */
class BuildProperties(propertiesFilePath: String = "/build.properties") extends LoggingSugar {

  /**
   * A new default properties file location, at `build.properties`, or None if no resource exists with that name
   */
  private val buildUrl = Option(getClass.getResource(propertiesFilePath))

  // at first use, try to retrieve a Properties object
  private lazy val props: Option[Properties] = {
    buildUrl.flatMap { url =>
      try {
        val stream = url.openStream()
        val p = new Properties
        try {
          p.load(stream)
          Some(p)
        } finally {
          stream.close()
        }
      } catch {
        case ioe: IOException =>
          getLogger[BuildProperties].warn(s"Unable to load $propertiesFilePath", ioe)
          None
      }
    }
  }

  /**
   * Retrieves an optional value from a `java.util.Properties` object
   * @param key the key to retrieve
   * @return an optional String value for the given `key`
   */
  def get(key: String): Option[String] = props.flatMap(p => Option(p.getProperty(key)))

}
