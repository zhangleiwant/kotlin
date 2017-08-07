// !DIAGNOSTICS: -UNUSED_VARIABLE -UNUSED_PARAMETER
// WARNING_FOR_JSR305_ANNOTATIONS

// FILE: spr/Nullable.java

package spr;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierNickname;
import javax.annotation.meta.When;

@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Nonnull(when = When.MAYBE)
@TypeQualifierNickname
public @interface Nullable {
}

// FILE: spr/NonNullApi.java

package spr;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;

@Target(ElementType.CLASS)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Nonnull
@TypeQualifierDefault({ElementType.METHOD, ElementType.PARAMETER})
public @interface NonNullApi {
}

// FILE: A.java

import spr.*;

@NonNullApi
public class A {
    public String field = null;

    public String foo(String x, @Nullable CharSequence y) {
        return "";
    }

    public String bar() {
        return "";
    }

    @Nullable
    public java.util.List<String> baz() {
        return null;
    }
}

// FILE: main.kt

fun main(a: A) {
    a.foo("", null)<!UNNECESSARY_SAFE_CALL!>?.<!>length
    a.foo("", null).length
    a.foo(<!NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS!>null<!>, "").length

    a.bar().length
    a.bar()<!UNNECESSARY_NOT_NULL_ASSERTION!>!!<!>.length

    a.field?.length
    a.field.length

    <!NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS!>a.baz()<!>.get(0)
    a.baz()!!.get(0).get(0)
    a.baz()!!.get(0)?.get(0)
}
