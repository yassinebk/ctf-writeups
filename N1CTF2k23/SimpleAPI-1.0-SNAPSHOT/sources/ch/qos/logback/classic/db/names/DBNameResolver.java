package ch.qos.logback.classic.db.names;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/logback-classic-1.2.3.jar:ch/qos/logback/classic/db/names/DBNameResolver.class */
public interface DBNameResolver {
    <N extends Enum<?>> String getTableName(N n);

    <N extends Enum<?>> String getColumnName(N n);
}
