/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.internal;

import static java.util.Collections.unmodifiableList;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.ArrayList;
import java.util.List;

// immutable
class IterableDiff {

  private final ComparisonStrategy comparisonStrategy;

  List<Object> unexpected;
  List<Object> missing;

  <T> IterableDiff(Iterable<T> actual, Iterable<T> expected, ComparisonStrategy comparisonStrategy) {
    this.comparisonStrategy = comparisonStrategy;
    // return the elements in actual that are not in expected: actual - expected
    this.unexpected = unexpectedActualElements(actual, expected);
    // return the elements in expected that are not in actual: expected - actual
    this.missing = missingActualElements(actual, expected);
  }

  static <T> IterableDiff diff(Iterable<T> actual, Iterable<T> expected, ComparisonStrategy comparisonStrategy) {
    return new IterableDiff(actual, expected, comparisonStrategy);
  }

  boolean differencesFound() {
    return !unexpected.isEmpty() || !missing.isEmpty();
  }

  /**
   * Returns the list of elements in the first iterable that are not in the second, i.e. first - second
   *
   * @param <T> the element type
   * @param actual the list we want to subtract from
   * @param expected the list to subtract
   * @return the list of elements in the first iterable that are not in the second, i.e. first - second
   */
  private <T> List<Object> unexpectedActualElements(Iterable<T> actual, Iterable<T> expected) {
    List<Object> missingInFirst = new ArrayList<>();
    // use a copy to deal correctly with potential duplicates
    List<T> copyOfExpected = newArrayList(expected);
    for (Object elementInActual : actual) {
      if (isActualElementInExpected(elementInActual, copyOfExpected)) {
        // remove the element otherwise a duplicate would be found in the case if there is one in actual
        iterablesRemoveFirst(copyOfExpected, elementInActual);
      } else {
        missingInFirst.add(elementInActual);
      }
    }
    return unmodifiableList(missingInFirst);
  }

  private <T> boolean isActualElementInExpected(Object elementInActual, List<T> copyOfExpected) {
    // the order of comparisonStrategy.areEqual is important if element comparison is not symmetrical, we must compare actual to
    // expected but not expected to actual, for ex recursive comparison where:
    // - actual element is PersonDto, expected a list of Person
    // - Person has more fields than PersonDto => comparing PersonDto to Person is ok as it looks at PersonDto fields only,
    // --- the opposite always fails as the reference fields are Person fields and PersonDto does not have all of them.
    return copyOfExpected.stream().anyMatch(expectedElement -> comparisonStrategy.areEqual(elementInActual, expectedElement));
  }

  private <T> List<Object> missingActualElements(Iterable<T> actual, Iterable<T> expected) {
    List<Object> missingInExpected = new ArrayList<>();
    // use a copy to deal correctly with potential duplicates
    List<T> copyOfActual = newArrayList(actual);
    for (Object expectedElement : expected) {
      if (iterableContains(copyOfActual, expectedElement)) {
        // remove the element otherwise a duplicate would be found in the case if there is one in actual
        iterablesRemoveFirst(copyOfActual, expectedElement);
      } else {
        missingInExpected.add(expectedElement);
      }
    }
    return unmodifiableList(missingInExpected);
  }

  private boolean iterableContains(Iterable<?> actual, Object expectedElement) {
    return comparisonStrategy.iterableContains(actual, expectedElement);
  }

  private void iterablesRemoveFirst(Iterable<?> actual, Object value) {
    comparisonStrategy.iterablesRemoveFirst(actual, value);
  }
}
