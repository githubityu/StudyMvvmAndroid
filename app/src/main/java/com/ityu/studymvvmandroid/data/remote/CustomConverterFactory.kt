import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import com.google.gson.Gson // Import Gson
import com.ityu.studymvvmandroid.data.remote.HttpResult
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class CustomConverterFactory : Converter.Factory() {

    private val gson: Gson = Gson()

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {

        if (type is ParameterizedType) {
            if (type.rawType == HttpResult::class.java) {
                val actualTypeArgument = type.actualTypeArguments[0]

                return Converter<ResponseBody, HttpResult<*>> { responseBody ->
                    try {
                        val stringResponse = responseBody.string()

                        gson.fromJson(stringResponse, type) as? HttpResult<*> ?: run {
                            val clazz = actualTypeArgument as Class<*>

                            @Suppress("UNCHECKED_CAST")
                            val data = gson.fromJson(stringResponse, clazz)
                            // Explicitly tell HttpResult what type T it should be
                            @Suppress("UNCHECKED_CAST")
                            (HttpResult<Any>(HttpResult.HTTP_SUCCESS, "Success").apply { this.data = data }) as HttpResult<*>
                        }
                    } finally {
                        responseBody.close()
                    }
                }
            }
        }
        return null
    }
}