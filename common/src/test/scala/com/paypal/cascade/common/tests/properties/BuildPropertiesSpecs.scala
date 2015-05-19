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
package com.paypal.cascade.common.tests.properties

import org.specs2._

import com.paypal.cascade.common.properties.BuildProperties
import com.paypal.cascade.common.tests.util.CommonImmutableSpecificationContext

/**
 * Tests [[com.paypal.cascade.common.properties.BuildProperties]]
 */
class BuildPropertiesSpecs extends Specification { override def is = s2"""

  BuildProperties loads the build.properties files.

  Get should:
    get the value when the path exists      ${GetValue().ok}

  """

  trait Context extends CommonImmutableSpecificationContext {
    // Nothing for now
  }

  case class GetValue() extends Context {
    def ok = apply {
      val bp = new BuildProperties("/test_build.properties")
      bp.get("test") must beSome("foo")
    }
  }

}
