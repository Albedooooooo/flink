/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.configuration;

import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.annotation.docs.ConfigGroup;
import org.apache.flink.annotation.docs.ConfigGroups;
import org.apache.flink.annotation.docs.Documentation;
import org.apache.flink.configuration.description.Description;

import java.time.Duration;

import static org.apache.flink.configuration.description.LinkElement.link;
import static org.apache.flink.configuration.description.TextElement.code;
import static org.apache.flink.configuration.description.TextElement.text;

/**
 * Config options for restart strategies.
 *
 * <p>{@link CleanupOptions} copied this collection of parameters to provide similar user
 * experience. FLINK-26359 is created to clean this up.
 */
@PublicEvolving
@ConfigGroups(
        groups = {
            @ConfigGroup(
                    name = "ExponentialDelayRestartStrategy",
                    keyPrefix = "restart-strategy.exponential-delay"),
            @ConfigGroup(
                    name = "FixedDelayRestartStrategy",
                    keyPrefix = "restart-strategy.fixed-delay"),
            @ConfigGroup(
                    name = "FailureRateRestartStrategy",
                    keyPrefix = "restart-strategy.failure-rate")
        })
public class RestartStrategyOptions {

    public static final ConfigOption<String> RESTART_STRATEGY =
            ConfigOptions.key("restart-strategy.type")
                    .stringType()
                    .noDefaultValue()
                    .withDeprecatedKeys("restart-strategy")
                    .withDescription(
                            Description.builder()
                                    .text(
                                            "Defines the restart strategy to use in case of job failures.")
                                    .linebreak()
                                    .text("Accepted values are:")
                                    .list(
                                            text(
                                                    "%s, %s, %s: No restart strategy.",
                                                    code("none"), code("off"), code("disable")),
                                            text(
                                                    "%s, %s: Fixed delay restart strategy. More details can be found %s.",
                                                    code("fixeddelay"),
                                                    code("fixed-delay"),
                                                    link(
                                                            "{{.Site.BaseURL}}{{.Site.LanguagePrefix}}/docs/ops/state/task_failure_recovery#fixed-delay-restart-strategy",
                                                            "here")),
                                            text(
                                                    "%s, %s: Failure rate restart strategy. More details can be found %s.",
                                                    code("failurerate"),
                                                    code("failure-rate"),
                                                    link(
                                                            "{{.Site.BaseURL}}{{.Site.LanguagePrefix}}/docs/ops/state/task_failure_recovery#failure-rate-restart-strategy",
                                                            "here")),
                                            text(
                                                    "%s, %s: Exponential delay restart strategy. More details can be found %s.",
                                                    code("exponentialdelay"),
                                                    code("exponential-delay"),
                                                    link(
                                                            "{{.Site.BaseURL}}{{.Site.LanguagePrefix}}/docs/ops/state/task_failure_recovery#exponential-delay-restart-strategy",
                                                            "here")))
                                    .text(
                                            "If checkpointing is disabled, the default value is %s. "
                                                    + "If checkpointing is enabled, the default value is %s with %s restart attempts and '%s' delay.",
                                            code("none"),
                                            code("fixed-delay"),
                                            code("Integer.MAX_VALUE"),
                                            code("1 s"))
                                    .build());

    public static final ConfigOption<Integer> RESTART_STRATEGY_FIXED_DELAY_ATTEMPTS =
            ConfigOptions.key("restart-strategy.fixed-delay.attempts")
                    .intType()
                    .defaultValue(1)
                    .withDescription(
                            Description.builder()
                                    .text(
                                            "The number of times that Flink retries the execution before the job is declared as failed if %s has been set to %s.",
                                            code(RESTART_STRATEGY.key()), code("fixed-delay"))
                                    .build());

    public static final ConfigOption<Duration> RESTART_STRATEGY_FIXED_DELAY_DELAY =
            ConfigOptions.key("restart-strategy.fixed-delay.delay")
                    .durationType()
                    .defaultValue(Duration.ofSeconds(1))
                    .withDescription(
                            Description.builder()
                                    .text(
                                            "Delay between two consecutive restart attempts if %s has been set to %s. "
                                                    + "Delaying the retries can be helpful when the program interacts with external systems where "
                                                    + "for example connections or pending transactions should reach a timeout before re-execution "
                                                    + "is attempted. It can be specified using notation: \"1 min\", \"20 s\"",
                                            code(RESTART_STRATEGY.key()), code("fixed-delay"))
                                    .build());

    public static final ConfigOption<Integer>
            RESTART_STRATEGY_FAILURE_RATE_MAX_FAILURES_PER_INTERVAL =
                    ConfigOptions.key("restart-strategy.failure-rate.max-failures-per-interval")
                            .intType()
                            .defaultValue(1)
                            .withDescription(
                                    Description.builder()
                                            .text(
                                                    "Maximum number of restarts in given time interval before failing a job if %s has been set to %s.",
                                                    code(RESTART_STRATEGY.key()),
                                                    code("failure-rate"))
                                            .build());

    public static final ConfigOption<Duration> RESTART_STRATEGY_FAILURE_RATE_FAILURE_RATE_INTERVAL =
            ConfigOptions.key("restart-strategy.failure-rate.failure-rate-interval")
                    .durationType()
                    .defaultValue(Duration.ofMinutes(1))
                    .withDescription(
                            Description.builder()
                                    .text(
                                            "Time interval for measuring failure rate if %s has been set to %s. "
                                                    + "It can be specified using notation: \"1 min\", \"20 s\"",
                                            code(RESTART_STRATEGY.key()), code("failure-rate"))
                                    .build());

    public static final ConfigOption<Duration> RESTART_STRATEGY_FAILURE_RATE_DELAY =
            ConfigOptions.key("restart-strategy.failure-rate.delay")
                    .durationType()
                    .defaultValue(Duration.ofSeconds(1))
                    .withDescription(
                            Description.builder()
                                    .text(
                                            "Delay between two consecutive restart attempts if %s has been set to %s. "
                                                    + "It can be specified using notation: \"1 min\", \"20 s\"",
                                            code(RESTART_STRATEGY.key()), code("failure-rate"))
                                    .build());

    public static final ConfigOption<Duration> RESTART_STRATEGY_EXPONENTIAL_DELAY_INITIAL_BACKOFF =
            ConfigOptions.key("restart-strategy.exponential-delay.initial-backoff")
                    .durationType()
                    .defaultValue(Duration.ofSeconds(1))
                    .withDescription(
                            Description.builder()
                                    .text(
                                            "Starting duration between restarts if %s has been set to %s. "
                                                    + "It can be specified using notation: \"1 min\", \"20 s\"",
                                            code(RESTART_STRATEGY.key()), code("exponential-delay"))
                                    .build());

    public static final ConfigOption<Duration> RESTART_STRATEGY_EXPONENTIAL_DELAY_MAX_BACKOFF =
            ConfigOptions.key("restart-strategy.exponential-delay.max-backoff")
                    .durationType()
                    .defaultValue(Duration.ofMinutes(1))
                    .withDescription(
                            Description.builder()
                                    .text(
                                            "The highest possible duration between restarts if %s has been set to %s. "
                                                    + "It can be specified using notation: \"1 min\", \"20 s\"",
                                            code(RESTART_STRATEGY.key()), code("exponential-delay"))
                                    .build());

    public static final ConfigOption<Double> RESTART_STRATEGY_EXPONENTIAL_DELAY_BACKOFF_MULTIPLIER =
            ConfigOptions.key("restart-strategy.exponential-delay.backoff-multiplier")
                    .doubleType()
                    .defaultValue(1.5)
                    .withDescription(
                            Description.builder()
                                    .text(
                                            "Backoff value is multiplied by this value after every failure,"
                                                    + "until max backoff is reached if %s has been set to %s.",
                                            code(RESTART_STRATEGY.key()), code("exponential-delay"))
                                    .build());

    public static final ConfigOption<Duration>
            RESTART_STRATEGY_EXPONENTIAL_DELAY_RESET_BACKOFF_THRESHOLD =
                    ConfigOptions.key("restart-strategy.exponential-delay.reset-backoff-threshold")
                            .durationType()
                            .defaultValue(Duration.ofHours(1))
                            .withDescription(
                                    Description.builder()
                                            .text(
                                                    "Threshold when the backoff is reset to its initial value if %s has been set to %s. "
                                                            + "It specifies how long the job must be running without failure "
                                                            + "to reset the exponentially increasing backoff to its initial value. "
                                                            + "It can be specified using notation: \"1 min\", \"20 s\"",
                                                    code(RESTART_STRATEGY.key()),
                                                    code("exponential-delay"))
                                            .build());

    public static final ConfigOption<Double> RESTART_STRATEGY_EXPONENTIAL_DELAY_JITTER_FACTOR =
            ConfigOptions.key("restart-strategy.exponential-delay.jitter-factor")
                    .doubleType()
                    .defaultValue(0.1)
                    .withDescription(
                            Description.builder()
                                    .text(
                                            "Jitter specified as a portion of the backoff if %s has been set to %s. "
                                                    + "It represents how large random value will be added or subtracted to the backoff. "
                                                    + "Useful when you want to avoid restarting multiple jobs at the same time.",
                                            code(RESTART_STRATEGY.key()), code("exponential-delay"))
                                    .build());

    @Documentation.OverrideDefault("infinite")
    public static final ConfigOption<Integer> RESTART_STRATEGY_EXPONENTIAL_DELAY_ATTEMPTS =
            ConfigOptions.key("restart-strategy.exponential-delay.attempts-before-reset-backoff")
                    .intType()
                    .defaultValue(Integer.MAX_VALUE)
                    .withDescription(
                            Description.builder()
                                    .text(
                                            "The number of times that Flink retries the execution before failing the job if %s has been set to %s. "
                                                    + "The number will be reset once the backoff is reset to its initial value.",
                                            code(RESTART_STRATEGY.key()), code("exponential-delay"))
                                    .build());

    private RestartStrategyOptions() {
        throw new UnsupportedOperationException("This class should never be instantiated.");
    }
}
