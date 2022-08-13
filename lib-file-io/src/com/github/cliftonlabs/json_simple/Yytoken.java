/* Copyright 2016 Clifton Labs
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */
package com.github.cliftonlabs.json_simple;

/**
 * Represents structural entities in JSON.
 * 
 * @since 2.0.0
 */
class Yytoken {
    /** Represents the different kinds of tokens. */
    enum Types {
	/** Tokens of this type will always have a value of ":" */
	COLON,
	/** Tokens of this type will always have a value of "," */
	COMMA,
	/**
	 * Tokens of this type will always have a value that is a boolean, null, number,
	 * or string.
	 */
	DATUM,
	/** Tokens of this type will always have a value of "" */
	END,
	/** Tokens of this type will always have a value of "{" */
	LEFT_BRACE,
	/** Tokens of this type will always have a value of "[" */
	LEFT_SQUARE,
	/** Tokens of this type will always have a value of "}" */
	RIGHT_BRACE,
	/** Tokens of this type will always have a value of "]" */
	RIGHT_SQUARE;
    }

    @SuppressWarnings("javadoc")
    private final Types type;
    @SuppressWarnings("javadoc")
    private final Object value;

    /**
     * @param nType  represents the kind of token the instantiated token will be.
     * @param nValue represents the value the token is associated with, will be
     *               ignored unless type is equal to Types.DATUM.
     * @see Types
     */
    Yytoken(final Types nType, final Object nValue) {
	/*
	 * Sanity check. Make sure the value is ignored for the proper value unless it
	 * is a datum token.
	 */
	switch (nType) {
	case COLON:
	    this.value = ":";
	    break;
	case COMMA:
	    this.value = ",";
	    break;
	case END:
	    this.value = "";
	    break;
	case LEFT_BRACE:
	    this.value = "{";
	    break;
	case LEFT_SQUARE:
	    this.value = "[";
	    break;
	case RIGHT_BRACE:
	    this.value = "}";
	    break;
	case RIGHT_SQUARE:
	    this.value = "]";
	    break;
	default:
	    this.value = nValue;
	    break;
	}
	this.type = nType;
    }

    /**
     * @return which of the {@link Yytoken.Types} the token is.
     * @see Yytoken.Types
     */
    Types getType() {
	return this.type;
    }

    /**
     * @return what the token is.
     * @see Types
     */
    Object getValue() {
	return this.value;
    }

    @Override
    public String toString() {
	final StringBuilder sb = new StringBuilder();
	sb.append(this.type.toString()).append("(").append(this.value).append(")");
	return sb.toString();
    }
}
