/**
 * Copyright 2013-2014 PayPal
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
package com.paypal.cascade.http.server

import com.paypal.cascade.common.properties.BuildProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.paypal.cascade.common.logging.LoggingSugar

/**
 * Container for the information generated by a `status` endpoint request
 * @param status the status
 * @param serviceName the name
 * @param dependencies the dependencies used
 * @param gitInfo the repo info for this build
 */
case class StatusResponse(
  status: String,
  @JsonProperty("service-name")
  serviceName: String,
  dependencies: List[String],
  @JsonProperty("git-info")
  gitInfo: Map[String, String])

object StatusResponse extends LoggingSugar {

  private lazy val logger = getLogger[StatusResponse.type]

  private val gitPropertyMappings = Map(
    "git.branch" -> "branch",
    "git.branch.clean" -> "branch-is-clean",
    "git.commit.sha" -> "commit-sha",
    "git.commit.date" -> "commit-date"
  )

  /**
   * Generate a StatusResponse based on current information
   * @param props the BuildProperties instance to read
   * @param serviceName the name
   * @return a StatusResponse object
   */
  def getStatusResponse(props: BuildProperties, serviceName: String): StatusResponse = {
    val dependencies = props.get("service.dependencies").map(_.split(",")).getOrElse(Array())
    val gitInfo = gitPropertyMappings.keys.foldLeft(Map[String, String]()) { (m, k) =>
      props.get(k) match {
        case Some(v) => m ++ Map(gitPropertyMappings(k) -> v)
        case None => m
      }
    }
    val status = StatusResponse("ok", serviceName, dependencies.toList, gitInfo)
    logger.debug(s"Status Response - status: ${status.status}, dependencies: ${dependencies.mkString(",")}, git-info: ${gitInfo.mkString(",")}")
    status
  }

}