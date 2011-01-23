/*
 * Created on Oct 18, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.error;

/**
 * Creates an error message indicating that an assertion that verifies that two values that represent size are equal
 * failed.
 *
 * @author Alex Ruiz
 */
public class DoesNotHaveSize extends BasicErrorMessage {

  /**
   * Creates a new </code>{@link DoesNotHaveSize}</code>.
   * @param actual the actual value in the failed assertion.
   * @param actualSize the size of {@code actual}.
   * @param expectedSize the expected size.
   * @return the created {@code ErrorMessage}.
   */
  public static ErrorMessage doesNotHaveSize(Object actual, Object actualSize, Object expectedSize) {
    return new DoesNotHaveSize("expected size:<%s> but was:<%s> in:<%s>", expectedSize, actualSize, actual);
  }

  private DoesNotHaveSize(String format, Object... arguments) {
    super(format, arguments);
  }
}
