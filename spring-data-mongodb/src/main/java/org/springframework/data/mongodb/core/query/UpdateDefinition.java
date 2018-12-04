/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.mongodb.core.query;

import org.bson.Document;

/**
 * Interface fixing must have operations for {@literal updates} as implemented via {@link Update}.
 *
 * @author Christoph Strobl
 * @since 2.2
 */
public interface UpdateDefinition {

	/**
	 * If {@literal true} prevents a write operation that affects <strong>multiple</strong> documents from yielding to
	 * other reads or writes once the first document is written. <br />
	 *
	 * @return {@literal true} if update isolated is set.
	 */
	Boolean isIsolated();

	/**
	 * @return the actual update in its native {@link Document} format. Never {@literal null}.
	 */
	Document getUpdateObject();

	/**
	 * Check if a given {@literal key} is modified by applying the update.
	 *
	 * @param key must not be {@literal null}.
	 * @return {@literal true} if the actual {@link UpdateDefinition} attempts to modify the given {@literal key}.
	 */
	boolean modifies(String key);

	/**
	 * Bump the version of a given {@literal key} by {@code 1}.
	 *
	 * @param key must not be {@literal null}.
	 */
	void incVersion(String key);
}
