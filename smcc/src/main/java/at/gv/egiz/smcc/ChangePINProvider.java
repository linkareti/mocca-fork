/*
* Copyright 2008 Federal Chancellery Austria and
* Graz University of Technology
*
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
*/
package at.gv.egiz.smcc;

/**
 *
 * @author Clemens Orthacker <clemens.orthacker@iaik.tugraz.at>
 */
public interface ChangePINProvider extends PINProvider {

  /**
   *
   * @param spec
   * @param retries
   * @return null if no old value for this pin
   * @throws at.gv.egiz.smcc.CancelledException if cancelled by user
   * @throws java.lang.InterruptedException
   */
  public char[] provideOldPIN(PINSpec spec, int retries)
          throws CancelledException, InterruptedException;

}
