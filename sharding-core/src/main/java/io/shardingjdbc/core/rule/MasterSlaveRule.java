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

package io.shardingjdbc.core.rule;

import com.google.common.base.Preconditions;
import io.shardingjdbc.core.api.algorithm.masterslave.MasterSlaveLoadBalanceAlgorithm;
import io.shardingjdbc.core.api.algorithm.masterslave.MasterSlaveLoadBalanceAlgorithmType;
import io.shardingjdbc.core.api.config.MasterSlaveRuleConfiguration;
import lombok.Getter;

import java.util.Collection;

/**
 * Databases and tables master-slave rule configuration.
 * 
 * @author zhangliang
 */
@Getter
public final class MasterSlaveRule {
    
    private final String name;
    
    private final String masterDataSourceName;
    
    private final Collection<String> slaveDataSourceNames;
    
    private final MasterSlaveLoadBalanceAlgorithm loadBalanceAlgorithm;
    
    public MasterSlaveRule(final MasterSlaveRuleConfiguration config) {
        name = config.getName();
        masterDataSourceName = config.getMasterDataSourceName();
        slaveDataSourceNames = config.getSlaveDataSourceNames();
        loadBalanceAlgorithm = null == config.getLoadBalanceAlgorithm() ? MasterSlaveLoadBalanceAlgorithmType.getDefaultAlgorithmType().getAlgorithm() : config.getLoadBalanceAlgorithm();
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(masterDataSourceName);
        Preconditions.checkNotNull(slaveDataSourceNames);
        Preconditions.checkState(!slaveDataSourceNames.isEmpty());
    }
}
