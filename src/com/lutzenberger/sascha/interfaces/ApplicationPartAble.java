/*
 * Copyright (c) 2016. Sascha Lutzenberger. All rights reserved.
 *
 * This file is part of the project "Abload_Tool"
 *
 * Redistribution and use in source and binary forms, without modification,
 * are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * - The author of this source code has given you the permission to use this
 *   source code.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * - The code is not used in commercial projects, except you got the permission
 *   for using the code in any commerical projects from the author.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.lutzenberger.sascha.interfaces;

/**
 * This is an interface which has to be implemented by all controllers which want to be able to deal with the
 * application parts of the application.
 *
 * With this implementation it is possible to use all Methods declared in
 * com.lutzenberger.sascha.interfaces.LoadApplicationParts
 *
 * This enables the controller to be able to switch the screen and get the working directory property of the
 * application.
 *
 * @see LoadApplicationParts
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 02.01.2016
 */

public interface ApplicationPartAble {
    /**
     * This method is used to initialize the controller with the ApplicationParts that are necessary for the entire
     * application. This enables the implementing class to be able to change the screens and get the working
     * directory property
     *
     * @param loadApplicationParts The application parts which are necessary for the program to run.
     */
    void setLoadApplicationParts(LoadApplicationParts loadApplicationParts);
}
