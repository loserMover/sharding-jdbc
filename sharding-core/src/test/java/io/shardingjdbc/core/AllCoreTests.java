/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.shardingjdbc.core;

import io.shardingjdbc.core.api.AllApiTests;
import io.shardingjdbc.core.constant.AllConstantsTests;
import io.shardingjdbc.core.hint.AllHintTests;
import io.shardingjdbc.core.merger.AllMergerTests;
import io.shardingjdbc.core.parsing.AllParsingTests;
import io.shardingjdbc.core.rewrite.AllRewriteTests;
import io.shardingjdbc.core.routing.AllRoutingTests;
import io.shardingjdbc.core.util.AllUtilTests;
import io.shardingjdbc.core.yaml.AllYamlTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
        AllConstantsTests.class, 
        AllApiTests.class, 
        AllUtilTests.class, 
        AllParsingTests.class, 
        AllRewriteTests.class, 
        AllRoutingTests.class, 
        AllMergerTests.class, 
        AllHintTests.class, 
        AllYamlTests.class
})
public class AllCoreTests {
}
