package com.example.selfiepal.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.selfiepal.model.CommentModel
import com.example.selfiepal.network.NetworkService
import com.example.selfiepal.utilities.ButtonState
import com.example.selfiepal.utilities.UIState
import com.example.selfiepal.utilities.hasInternetConnection
import com.example.selfiepal.utilities.noInternetConnection
import com.example.selfiepal.utilities.somethingwentwrong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    application: Application,
    private val postService: NetworkService,
):AndroidViewModel(application) {

    private val _commentsListState = MutableStateFlow<UIState>(UIState.Loading)
    val commentsListState: StateFlow<UIState> = _commentsListState.asStateFlow()
    private val _buttonState = MutableStateFlow(ButtonState(loading = false))
    val buttonState: StateFlow<ButtonState> = _buttonState.asStateFlow()


    suspend fun getCommentByPostId(postId:String){
        if (hasInternetConnection(getApplication())){
            try {
                val response = postService.getCommentByPostId(postId)
                _commentsListState.value = UIState.Success(data = response.body()!!.data)
            }catch (e:Exception){
                Log.d("error", e.message.toString())
                _commentsListState.value = UIState.Error(e.message.toString())
            }
        }else{
            _commentsListState.value = UIState.Error(noInternetConnection)
        }
    }

    suspend fun createComment(comment: CommentModel) {
        if (hasInternetConnection(getApplication())) {
            try {
                _buttonState.value = ButtonState(true)
                val response = postService.createComment(comment)
                Log.d("[Logs]", response.code().toString())
                _buttonState.value = ButtonState(
                    false,
                    message = response.body()!!.message
                )
            } catch (e: Exception) {
                Log.d("error", e.message.toString())
                _buttonState.value = ButtonState(
                    false,
                    message = somethingwentwrong
                )
            }
        } else {
            _buttonState.value = ButtonState(
                false,
                message = noInternetConnection
            )
        }
    }

    suspend fun deleteComment(commentId: String) {
        if (hasInternetConnection(getApplication())) {
            try {
                _buttonState.value = ButtonState(true)
                val response = postService.deleteComment(commentId)
                Log.d("[Logs]", response.code().toString())
                _buttonState.value = ButtonState(
                    false,
                    message = response.body()!!.message)

            } catch (e: Exception) {
                Log.d("error", e.message.toString())
                _buttonState.value = ButtonState(
                    false,
                    message = somethingwentwrong
                )
            }
        } else {
            _buttonState.value = ButtonState(
                false,
                message = noInternetConnection
            )
        }
    }


}