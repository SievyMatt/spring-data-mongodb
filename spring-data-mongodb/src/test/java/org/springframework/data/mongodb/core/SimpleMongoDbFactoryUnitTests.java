/*
 * Copyright 2011-2017 the original author or authors.
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
package org.springframework.data.mongodb.core;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.test.util.ReflectionTestUtils.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.MongoDbFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

/**
 * Unit tests for {@link SimpleMongoDbFactory}.
 *
 * @author Oliver Gierke
 * @author Christoph Strobl
 */
@RunWith(MockitoJUnitRunner.class)
public class SimpleMongoDbFactoryUnitTests {

	public @Rule ExpectedException expectedException = ExpectedException.none();
	@Mock MongoClient mongo;

	@Test // DATADOC-254, DATAMONGO-1903
	public void rejectsIllegalDatabaseNames() {

		rejectsDatabaseName("foo.bar");
		rejectsDatabaseName("foo$bar");
		rejectsDatabaseName("foo\\bar");
		rejectsDatabaseName("foo//bar");
		rejectsDatabaseName("foo bar");
		rejectsDatabaseName("foo\"bar");
	}

	@Test // DATADOC-254
	@SuppressWarnings("deprecation")
	public void allowsDatabaseNames() {
		new SimpleMongoDbFactory(mongo, "foo-bar");
		new SimpleMongoDbFactory(mongo, "foo_bar");
		new SimpleMongoDbFactory(mongo, "foo01231bar");
	}

	@Test // DATADOC-295
	@SuppressWarnings("deprecation")
	public void mongoUriConstructor() {

		MongoClientURI mongoURI = new MongoClientURI("mongodb://myUsername:myPassword@localhost/myDatabase.myCollection");
		MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongoURI);

		assertThat(getField(mongoDbFactory, "databaseName").toString(), is("myDatabase"));
	}

	@Test // DATAMONGO-1158
	public void constructsMongoClientAccordingToMongoUri() {

		MongoClientURI uri = new MongoClientURI("mongodb://myUserName:myPassWord@127.0.0.1:27017/myDataBase.myCollection");
		SimpleMongoDbFactory factory = new SimpleMongoDbFactory(uri);

		assertThat(getField(factory, "databaseName").toString(), is("myDataBase"));
	}

	@SuppressWarnings("deprecation")
	private void rejectsDatabaseName(String databaseName) {
		assertThatThrownBy(() -> new SimpleMongoDbFactory(mongo, databaseName))
				.isInstanceOf(IllegalArgumentException.class);
	}
}
