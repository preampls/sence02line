package ls.example.t.zero2line.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2017/12/15
 *     desc  : utils about reflect
 * </pre>
 */
public final class ReflectUtils {

    private final Class<?> type;

    private final Object object;

    private ReflectUtils(final Class<?> type) {
        this(type, type);
    }

    private ReflectUtils(final Class<?> type, Object object) {
        this.type = type;
        this.object = object;
    }

    ///////////////////////////////////////////////////////////////////////////
    // reflect
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Reflect the class.
     *
     * @param className The name of class.
     * @return the single {@link ReflectUtils} instance
     * @throws ReflectException if reflect unsuccessfully
     */
    public static ls.example.t.zero2line.util.ReflectUtils reflect(final String className)
            throws ls.example.t.zero2line.util.ReflectUtils.ReflectException {
        return reflect(forName(className));
    }

    /**
     * Reflect the class.
     *
     * @param className   The name of class.
     * @param classLoader The loader of class.
     * @return the single {@link ReflectUtils} instance
     * @throws ReflectException if reflect unsuccessfully
     */
    public static ls.example.t.zero2line.util.ReflectUtils reflect(final String className, final ClassLoader classLoader)
            throws ls.example.t.zero2line.util.ReflectUtils.ReflectException {
        return reflect(forName(className, classLoader));
    }

    /**
     * Reflect the class.
     *
     * @param clazz The class.
     * @return the single {@link ReflectUtils} instance
     * @throws ReflectException if reflect unsuccessfully
     */
    public static ls.example.t.zero2line.util.ReflectUtils reflect(final Class<?> clazz)
            throws ls.example.t.zero2line.util.ReflectUtils.ReflectException {
        return new ls.example.t.zero2line.util.ReflectUtils(clazz);
    }

    /**
     * Reflect the class.
     *
     * @param object The object.
     * @return the single {@link ReflectUtils} instance
     * @throws ReflectException if reflect unsuccessfully
     */
    public static ls.example.t.zero2line.util.ReflectUtils reflect(final Object object)
            throws ls.example.t.zero2line.util.ReflectUtils.ReflectException {
        return new ls.example.t.zero2line.util.ReflectUtils(object == null ? Object.class : object.getClass(), object);
    }

    private static Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ls.example.t.zero2line.util.ReflectUtils.ReflectException(e);
        }
    }

    private static Class<?> forName(String name, ClassLoader classLoader) {
        try {
            return Class.forName(name, true, classLoader);
        } catch (ClassNotFoundException e) {
            throw new ls.example.t.zero2line.util.ReflectUtils.ReflectException(e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // newInstance
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Create and initialize a new instance.
     *
     * @return the single {@link ReflectUtils} instance
     */
    public ls.example.t.zero2line.util.ReflectUtils newInstance() {
        return newInstance(new Object[0]);
    }

    /**
     * Create and initialize a new instance.
     *
     * @param args The args.
     * @return the single {@link ReflectUtils} instance
     */
    public ls.example.t.zero2line.util.ReflectUtils newInstance(Object... args) {
        Class<?>[] types = getArgsType(args);
        try {
            java.lang.reflect.Constructor<?> constructor = type().getDeclaredConstructor(types);
            return newInstance(constructor, args);
        } catch (NoSuchMethodException e) {
            java.util.List<java.lang.reflect.Constructor<?>> list = new java.util.ArrayList<>();
            for (java.lang.reflect.Constructor<?> constructor : type().getDeclaredConstructors()) {
                if (match(constructor.getParameterTypes(), types)) {
                    list.add(constructor);
                }
            }
            if (list.isEmpty()) {
                throw new ls.example.t.zero2line.util.ReflectUtils.ReflectException(e);
            } else {
                sortConstructors(list);
                return newInstance(list.get(0), args);
            }
        }
    }

    private Class<?>[] getArgsType(final Object... args) {
        if (args == null) return new Class[0];
        Class<?>[] result = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            Object value = args[i];
            result[i] = value == null ? ls.example.t.zero2line.util.ReflectUtils.NULL.class : value.getClass();
        }
        return result;
    }

    private void sortConstructors(java.util.List<java.lang.reflect.Constructor<?>> list) {
        java.util.Collections.sort(list, new java.util.Comparator<java.lang.reflect.Constructor<?>>() {
            @Override
            public int compare(java.lang.reflect.Constructor<?> o1, java.lang.reflect.Constructor<?> o2) {
                Class<?>[] types1 = o1.getParameterTypes();
                Class<?>[] types2 = o2.getParameterTypes();
                int len = types1.length;
                for (int i = 0; i < len; i++) {
                    if (!types1[i].equals(types2[i])) {
                        if (wrapper(types1[i]).isAssignableFrom(wrapper(types2[i]))) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                }
                return 0;
            }
        });
    }

    private ls.example.t.zero2line.util.ReflectUtils newInstance(final java.lang.reflect.Constructor<?> constructor, final Object... args) {
        try {
            return new ls.example.t.zero2line.util.ReflectUtils(
                    constructor.getDeclaringClass(),
                    accessible(constructor).newInstance(args)
            );
        } catch (Exception e) {
            throw new ls.example.t.zero2line.util.ReflectUtils.ReflectException(e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // field
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Get the field.
     *
     * @param name The name of field.
     * @return the single {@link ReflectUtils} instance
     */
    public ls.example.t.zero2line.util.ReflectUtils field(final String name) {
        try {
            java.lang.reflect.Field field = getField(name);
            return new ls.example.t.zero2line.util.ReflectUtils(field.getType(), field.get(object));
        } catch (IllegalAccessException e) {
            throw new ls.example.t.zero2line.util.ReflectUtils.ReflectException(e);
        }
    }

    /**
     * Set the field.
     *
     * @param name  The name of field.
     * @param value The value.
     * @return the single {@link ReflectUtils} instance
     */
    public ls.example.t.zero2line.util.ReflectUtils field(String name, Object value) {
        try {
            java.lang.reflect.Field field = getField(name);
            field.set(object, unwrap(value));
            return this;
        } catch (Exception e) {
            throw new ls.example.t.zero2line.util.ReflectUtils.ReflectException(e);
        }
    }

    private java.lang.reflect.Field getField(String name) throws IllegalAccessException {
        java.lang.reflect.Field field = getAccessibleField(name);
        if ((field.getModifiers() & java.lang.reflect.Modifier.FINAL) == java.lang.reflect.Modifier.FINAL) {
            try {
                java.lang.reflect.Field modifiersField = java.lang.reflect.Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
            } catch (NoSuchFieldException ignore) {
                // runs in android will happen
            }
        }
        return field;
    }

    private java.lang.reflect.Field getAccessibleField(String name) {
        Class<?> type = type();
        try {
            return accessible(type.getField(name));
        } catch (NoSuchFieldException e) {
            do {
                try {
                    return accessible(type.getDeclaredField(name));
                } catch (NoSuchFieldException ignore) {
                }
                type = type.getSuperclass();
            } while (type != null);
            throw new ls.example.t.zero2line.util.ReflectUtils.ReflectException(e);
        }
    }

    private Object unwrap(Object object) {
        if (object instanceof ls.example.t.zero2line.util.ReflectUtils) {
            return ((ls.example.t.zero2line.util.ReflectUtils) object).get();
        }
        return object;
    }

    ///////////////////////////////////////////////////////////////////////////
    // method
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Invoke the method.
     *
     * @param name The name of method.
     * @return the single {@link ReflectUtils} instance
     * @throws ReflectException if reflect unsuccessfully
     */
    public ls.example.t.zero2line.util.ReflectUtils method(final String name) throws ls.example.t.zero2line.util.ReflectUtils.ReflectException {
        return method(name, new Object[0]);
    }

    /**
     * Invoke the method.
     *
     * @param name The name of method.
     * @param args The args.
     * @return the single {@link ReflectUtils} instance
     * @throws ReflectException if reflect unsuccessfully
     */
    public ls.example.t.zero2line.util.ReflectUtils method(final String name, final Object... args) throws ls.example.t.zero2line.util.ReflectUtils.ReflectException {
        Class<?>[] types = getArgsType(args);
        try {
            java.lang.reflect.Method method = exactMethod(name, types);
            return method(method, object, args);
        } catch (NoSuchMethodException e) {
            try {
                java.lang.reflect.Method method = similarMethod(name, types);
                return method(method, object, args);
            } catch (NoSuchMethodException e1) {
                throw new ls.example.t.zero2line.util.ReflectUtils.ReflectException(e1);
            }
        }
    }

    private ls.example.t.zero2line.util.ReflectUtils method(final java.lang.reflect.Method method, final Object obj, final Object... args) {
        try {
            accessible(method);
            if (method.getReturnType() == void.class) {
                method.invoke(obj, args);
                return reflect(obj);
            } else {
                return reflect(method.invoke(obj, args));
            }
        } catch (Exception e) {
            throw new ls.example.t.zero2line.util.ReflectUtils.ReflectException(e);
        }
    }

    private java.lang.reflect.Method exactMethod(final String name, final Class<?>[] types)
            throws NoSuchMethodException {
        Class<?> type = type();
        try {
            return type.getMethod(name, types);
        } catch (NoSuchMethodException e) {
            do {
                try {
                    return type.getDeclaredMethod(name, types);
                } catch (NoSuchMethodException ignore) {
                }
                type = type.getSuperclass();
            } while (type != null);
            throw new NoSuchMethodException();
        }
    }

    private java.lang.reflect.Method similarMethod(final String name, final Class<?>[] types)
            throws NoSuchMethodException {
        Class<?> type = type();
        java.util.List<java.lang.reflect.Method> methods = new java.util.ArrayList<>();
        for (java.lang.reflect.Method method : type.getMethods()) {
            if (isSimilarSignature(method, name, types)) {
                methods.add(method);
            }
        }
        if (!methods.isEmpty()) {
            sortMethods(methods);
            return methods.get(0);
        }
        do {
            for (java.lang.reflect.Method method : type.getDeclaredMethods()) {
                if (isSimilarSignature(method, name, types)) {
                    methods.add(method);
                }
            }
            if (!methods.isEmpty()) {
                sortMethods(methods);
                return methods.get(0);
            }
            type = type.getSuperclass();
        } while (type != null);

        throw new NoSuchMethodException("No similar method " + name + " with params "
                + java.util.Arrays.toString(types) + " could be found on type " + type() + ".");
    }

    private void sortMethods(final java.util.List<java.lang.reflect.Method> methods) {
        java.util.Collections.sort(methods, new java.util.Comparator<java.lang.reflect.Method>() {
            @Override
            public int compare(java.lang.reflect.Method o1, java.lang.reflect.Method o2) {
                Class<?>[] types1 = o1.getParameterTypes();
                Class<?>[] types2 = o2.getParameterTypes();
                int len = types1.length;
                for (int i = 0; i < len; i++) {
                    if (!types1[i].equals(types2[i])) {
                        if (wrapper(types1[i]).isAssignableFrom(wrapper(types2[i]))) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                }
                return 0;
            }
        });
    }

    private boolean isSimilarSignature(final java.lang.reflect.Method possiblyMatchingMethod,
                                       final String desiredMethodName,
                                       final Class<?>[] desiredParamTypes) {
        return possiblyMatchingMethod.getName().equals(desiredMethodName)
                && match(possiblyMatchingMethod.getParameterTypes(), desiredParamTypes);
    }

    private boolean match(final Class<?>[] declaredTypes, final Class<?>[] actualTypes) {
        if (declaredTypes.length == actualTypes.length) {
            for (int i = 0; i < actualTypes.length; i++) {
                if (actualTypes[i] == ls.example.t.zero2line.util.ReflectUtils.NULL.class
                        || wrapper(declaredTypes[i]).isAssignableFrom(wrapper(actualTypes[i]))) {
                    continue;
                }
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    private <T extends java.lang.reflect.AccessibleObject> T accessible(T accessible) {
        if (accessible == null) return null;
        if (accessible instanceof java.lang.reflect.Member) {
            java.lang.reflect.Member member = (java.lang.reflect.Member) accessible;
            if (java.lang.reflect.Modifier.isPublic(member.getModifiers())
                    && java.lang.reflect.Modifier.isPublic(member.getDeclaringClass().getModifiers())) {
                return accessible;
            }
        }
        if (!accessible.isAccessible()) accessible.setAccessible(true);
        return accessible;
    }

    ///////////////////////////////////////////////////////////////////////////
    // proxy
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Create a proxy for the wrapped object allowing to typesafely invoke
     * methods on it using a custom interface.
     *
     * @param proxyType The interface type that is implemented by the proxy.
     * @return a proxy for the wrapped object
     */
    @SuppressWarnings("unchecked")
    public <P> P proxy(final Class<P> proxyType) {
        final boolean isMap = (object instanceof java.util.Map);
        final java.lang.reflect.InvocationHandler handler = new java.lang.reflect.InvocationHandler() {
            @Override
            @SuppressWarnings("null")
            public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) {
                String name = method.getName();
                try {
                    return reflect(object).method(name, args).get();
                } catch (ls.example.t.zero2line.util.ReflectUtils.ReflectException e) {
                    if (isMap) {
                        java.util.Map<String, Object> map = (java.util.Map<String, Object>) object;
                        int length = (args == null ? 0 : args.length);

                        if (length == 0 && name.startsWith("get")) {
                            return map.get(property(name.substring(3)));
                        } else if (length == 0 && name.startsWith("is")) {
                            return map.get(property(name.substring(2)));
                        } else if (length == 1 && name.startsWith("set")) {
                            map.put(property(name.substring(3)), args[0]);
                            return null;
                        }
                    }
                    throw e;
                }
            }
        };
        return (P) java.lang.reflect.Proxy.newProxyInstance(proxyType.getClassLoader(),
                new Class[]{proxyType},
                handler);
    }

    /**
     * Get the POJO property name of an getter/setter
     */
    private static String property(String string) {
        int length = string.length();

        if (length == 0) {
            return "";
        } else if (length == 1) {
            return string.toLowerCase();
        } else {
            return string.substring(0, 1).toLowerCase() + string.substring(1);
        }
    }

    private Class<?> type() {
        return type;
    }

    private Class<?> wrapper(final Class<?> type) {
        if (type == null) {
            return null;
        } else if (type.isPrimitive()) {
            if (boolean.class == type) {
                return Boolean.class;
            } else if (int.class == type) {
                return Integer.class;
            } else if (long.class == type) {
                return Long.class;
            } else if (short.class == type) {
                return Short.class;
            } else if (byte.class == type) {
                return Byte.class;
            } else if (double.class == type) {
                return Double.class;
            } else if (float.class == type) {
                return Float.class;
            } else if (char.class == type) {
                return Character.class;
            } else if (void.class == type) {
                return Void.class;
            }
        }
        return type;
    }

    /**
     * Get the result.
     *
     * @param <T> The value type.
     * @return the result
     */
    @SuppressWarnings("unchecked")
    public <T> T get() {
        return (T) object;
    }

    @Override
    public int hashCode() {
        return object.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ls.example.t.zero2line.util.ReflectUtils && object.equals(((ls.example.t.zero2line.util.ReflectUtils) obj).get());
    }

    @Override
    public String toString() {
        return object.toString();
    }

    private static class NULL {
    }

    public static class ReflectException extends RuntimeException {

        private static final long serialVersionUID = 858774075258496016L;

        public ReflectException(String message) {
            super(message);
        }

        public ReflectException(String message, Throwable cause) {
            super(message, cause);
        }

        public ReflectException(Throwable cause) {
            super(cause);
        }
    }
}
