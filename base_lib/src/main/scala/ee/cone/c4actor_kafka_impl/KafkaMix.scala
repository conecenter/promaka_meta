
package ee.cone.c4actor_kafka_impl

trait KafkaConfigAppBase
trait KafkaProducerAppBase extends KafkaConfigApp
trait KafkaConsumerAppBase extends KafkaConfigApp with LZ4DeCompressorApp
trait LZ4DeCompressorAppBase
trait LZ4RawCompressorAppBase
trait KafkaPurgerAppBase
