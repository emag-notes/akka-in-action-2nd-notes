package routers

import akka.actor.typed.scaladsl.{Behaviors, Routers}

object DataEnricher {
  sealed trait Command
  final case class Message(id: String, content: String) extends Command

  def apply() = Behaviors.setup[Command] { context =>
    val router = context.spawnAnonymous {
      Routers.group(Aggregator.serviceKey)
        .withConsistentHashingRouting(10, Aggregator.mapping)
    }

    Behaviors.receiveMessage[Command] {
      case Message(id, content) =>
        // fetches some metada and
        router ! Aggregator.Enriched(id, ":metadata")
        Behaviors.same
    }
  }
}
