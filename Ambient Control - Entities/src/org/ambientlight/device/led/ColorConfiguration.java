/*  Copyright 2013 Florian Bornkessel

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package org.ambientlight.device.led;

import java.io.Serializable;


/**
 * @author Florian Bornkessel
 */
public class ColorConfiguration implements Serializable {

	private static final long serialVersionUID = 1L;

	public float gammaRed = 1.0f;
	public float gammaGreen = 1.0f;
	public float gammaBlue = 1.0f;
	public float levelRed = 1.0f;
	public float levelGreen = 1.0f;
	public float levelBlue = 1.0f;

}