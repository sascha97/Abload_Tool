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

package com.lutzenberger.sascha.sax;

/**
 * Contains element children. Using this class instead of HashMap results in measurably better performance.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 08.09.2015
 */
class Children {

    Child[] children = new Child[16];

    /**
     * Looks up a child by name and creates a new one if necessary.
     */
    Element getOrCreate(Element parent, String uri, String localName) {
        int hash = uri.hashCode() * 31 + localName.hashCode();
        int index = hash & 15;

        Child current = children[index];
        if (current == null) {
            // We have no children in this bucket yet.
            current = new Child(parent, uri, localName, parent.depth + 1, hash);
            children[index] = current;
            return current;
        } else {
            // Search this bucket.
            Child previous;
            do {
                if (current.hash == hash
                        && current.uri.compareTo(uri) == 0
                        && current.localName.compareTo(localName) == 0) {
                    // We already have a child with that name.
                    return current;
                }

                previous = current;
                current = current.next;
            } while (current != null);

            // Add a new child to the bucket.
            current = new Child(parent, uri, localName, parent.depth + 1, hash);
            previous.next = current;
            return current;
        }
    }

    /**
     * Looks up a child by name.
     */
    Element get(String uri, String localName) {
        int hash = uri.hashCode() * 31 + localName.hashCode();
        int index = hash & 15;

        Child current = children[index];
        if (current == null) {
            return null;
        } else {
            do {
                if (current.hash == hash
                        && current.uri.compareTo(uri) == 0
                        && current.localName.compareTo(localName) == 0) {
                    return current;
                }
                current = current.next;
            } while (current != null);

            return null;
        }
    }

    static class Child extends Element {

        final int hash;
        Child next;

        Child(Element parent, String uri, String localName, int depth,
              int hash) {
            super(parent, uri, localName, depth);
            this.hash = hash;
        }
    }
}
