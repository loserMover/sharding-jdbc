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
import com.google.common.base.Strings;
import io.shardingjdbc.core.api.config.TableRuleConfiguration;
import io.shardingjdbc.core.exception.ShardingJdbcException;
import io.shardingjdbc.core.keygen.KeyGenerator;
import io.shardingjdbc.core.keygen.KeyGeneratorFactory;
import io.shardingjdbc.core.routing.strategy.ShardingStrategy;
import io.shardingjdbc.core.util.InlineExpressionParser;
import lombok.Getter;
import lombok.ToString;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Table rule configuration.
 * 
 * @author zhangliang
 */
@Getter
@ToString
public final class TableRule {
    
    private final String logicTable;
    
    private final List<DataNode> actualDataNodes;
    
    private final ShardingStrategy databaseShardingStrategy;
    
    private final ShardingStrategy tableShardingStrategy;
    
    private final String generateKeyColumn;
    
    private final KeyGenerator keyGenerator;
    
    private final String logicIndex;
    
    public TableRule(final TableRuleConfiguration tableRuleConfig, final Collection<String> dataSourceNames) {
        logicTable = tableRuleConfig.getLogicTable().toLowerCase();
        List<String> dataNodes = new InlineExpressionParser(tableRuleConfig.getActualDataNodes()).evaluate();
        actualDataNodes = isEmptyDataNodes(dataNodes) ? generateDataNodes(tableRuleConfig.getLogicTable(), dataSourceNames) : generateDataNodes(dataNodes, dataSourceNames);
        databaseShardingStrategy = null == tableRuleConfig.getDatabaseShardingStrategyConfig() ? null : tableRuleConfig.getDatabaseShardingStrategyConfig().build();
        tableShardingStrategy = null == tableRuleConfig.getTableShardingStrategyConfig() ? null : tableRuleConfig.getTableShardingStrategyConfig().build();
        generateKeyColumn = tableRuleConfig.getKeyGeneratorColumnName();
        keyGenerator = !hasKeyGenerator(tableRuleConfig) ? null : KeyGeneratorFactory.newInstance(tableRuleConfig.getKeyGeneratorClass());
        logicIndex = null == tableRuleConfig.getLogicIndex() ? null : tableRuleConfig.getLogicIndex().toLowerCase();
        Preconditions.checkNotNull(logicTable);
    }
    
    private boolean isEmptyDataNodes(final List<String> dataNodes) {
        return null == dataNodes || dataNodes.isEmpty();
    }
    
    private List<DataNode> generateDataNodes(final String logicTable, final Collection<String> dataSourceNames) {
        List<DataNode> result = new LinkedList<>();
        for (String each : dataSourceNames) {
            result.add(new DataNode(each, logicTable));
        }
        return result;
    }
    
    private List<DataNode> generateDataNodes(final List<String> actualDataNodes, final Collection<String> dataSourceNames) {
        List<DataNode> result = new LinkedList<>();
        for (String each : actualDataNodes) {
            DataNode dataNode = new DataNode(each);
            if (!dataSourceNames.contains(dataNode.getDataSourceName())) {
                throw new ShardingJdbcException("Cannot find data source in sharding rule, invalid actual data node is: '%s'", each);
            }
            result.add(dataNode);
        }
        return result;
    }
    
    private boolean hasKeyGenerator(final TableRuleConfiguration tableRuleConfig) {
        return !Strings.isNullOrEmpty(generateKeyColumn) && !Strings.isNullOrEmpty(tableRuleConfig.getKeyGeneratorClass());
    }
    
    /**
     * Get actual data source names.
     *
     * @return actual data source names
     */
    public Collection<String> getActualDatasourceNames() {
        Collection<String> result = new LinkedHashSet<>(actualDataNodes.size());
        for (DataNode each : actualDataNodes) {
            result.add(each.getDataSourceName());
        }
        return result;
    }
    
    /**
     * Get actual table names via target data source name.
     *
     * @param targetDataSource target data source name
     * @return names of actual tables
     */
    public Collection<String> getActualTableNames(final String targetDataSource) {
        Collection<String> result = new LinkedHashSet<>(actualDataNodes.size());
        for (DataNode each : actualDataNodes) {
            if (targetDataSource.equals(each.getDataSourceName())) {
                result.add(each.getTableName());
            }
        }
        return result;
    }
    
    int findActualTableIndex(final String dataSourceName, final String actualTableName) {
        int result = 0;
        for (DataNode each : actualDataNodes) {
            if (each.getDataSourceName().equalsIgnoreCase(dataSourceName) && each.getTableName().equalsIgnoreCase(actualTableName)) {
                return result;
            }
            result++;
        }
        return -1;
    }
    
    boolean isExisted(final String actualTableName) {
        for (DataNode each : actualDataNodes) {
            if (each.getTableName().equalsIgnoreCase(actualTableName)) {
                return true;
            }
        }
        return false;
    }
}
