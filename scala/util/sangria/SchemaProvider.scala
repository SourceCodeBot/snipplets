package util.sangria

import sangria.execution.deferred.Fetcher
import sangria.schema.Field

/**
  * this trait could use to provide the basic elements for creating a schema.
  * use case: a project will support multiple schemas behind one GraphQL endpoint.
  * how to use in this case: implement your custom trait extends SchemaProvider[YourContextType]
  *
  * now, in your maybe named "GraphQLSchema".scala file:
  * {{{
  * object GraphSchema  {
  *
  *   private object CustomSchema extends CustomSchemaSupport
  *
  *   val fetchers: Seq[Fetcher[Context, _, _, _]] = CustomSchema.getFetchers()
  *   val queryFields: Seq[Field[Context,Unit]] = CustomSchema.getQueryFields()
  *
  *
  *   /* -------------------------------------------------------------
  *    * fetchers
  *    * ------------------------------------------------------------- */
  *
  *   val Resolver = DeferredResolver.fetchers(fetchers :_*)
  *
  *   /* -------------------------------------------------------
  *    * query
  *    * ------------------------------------------------------ */
  *
  *   val QueryType = ObjectType(
  *     "Query",
  *     fields[Context, Unit](
  *       queryFields :_*
  *     )
  *   )
  *
  *   /* -------------------------------------------------------
  *    * mutations
  *    * ------------------------------------------------------ */
  *   // no Mutations in this case
  *
  *   val SchemaDefinition = Schema(QueryType)
  *
  * }
  * }}}
  *
  * @tparam Ctx context type
  * @author SourceCodeBot <dev@nils-heinemann.de> 18.12.2018.
  */
trait SchemaProvider[Ctx] {

  /**
    * supply your [[scala.collection.immutable.Seq]] of fields for the query type in your GraphQL endpoint
    * example:
    * {{{
    *   override def getQueryFields(): Seq[Field[Ctx,Unit]] = super.getQueryFields() ++ queryFields
    * }}}
    * @return [[scala.collection.immutale.Seq]] of [[sangria.schema.Field]]
    */
  def getQueryFields(): Seq[Field[Ctx,Unit]] = Seq.empty[Field[Ctx,Unit]]

  /**
    * supply your [[scala.collection.immutable.Seq]] of fields for the mutation type in your GraphQL endpoint
    * example:
    * {{{
    *   override def getMutationFields(): Seq[Field[Ctx,Unit]] = super.getMutationFields() ++ mutationFields
    * }}}
    * @return [[scala.collection.immutable.Seq]] of [[sangria.schema.Field]]
    */
  def getMutationFields(): Seq[Field[Ctx,Unit]] = Seq.empty[Field[Ctx,Unit]]

  /**
    * supply your [[scala.collection.immutable.Seq]] of fields for the subscripbtion type in your GraphQL endpoint
    * example:
    * {{{
    *   override def getSubscriptionFields(): Seq[Field[Ctx,Unit]] = super.getSubscriptionFields() ++ subscriptionFields
    * }}}
    * @return [[scala.collection.immutable.Seq]] of [[sangria.schema.Field]]
    */
  def getSubscriptionFields(): Seq[Field[Ctx,Unit]] = Seq.empty[Field[Ctx,Unit]]

  /**
    * supply your [[scala.collection.immutable.Seq]] of [[sangria.execution.deferred.Fetcher]] in your GraphQL endpoint
    * example:
    * {{{
    *   override def getFetchers(): Seq[Fetcher[Ctx, _, _, _]] = super.getFetchers() ++ Seq(myFetcher)
    * }}}
    * @return [[scala.collection.immutable.Seq]] of [[sangria.execution.deferred.Fetcher]]
    */
  def getFetchers(): Seq[Fetcher[Ctx, _, _, _]] = Seq.empty[Fetcher[Ctx, _,_,_]]

}