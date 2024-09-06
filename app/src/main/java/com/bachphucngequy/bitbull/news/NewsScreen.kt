package com.bachphucngequy.bitbull.news

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.bachphucngequy.bitbull.news.Dimens.ExtraSmallPadding2
import com.bachphucngequy.bitbull.news.Dimens.MediumPadding1
import com.bachphucngequy.bitbull.news.api.Article
import com.bachphucngequy.bitbull.news.api.NetworkResponse
import com.bachphucngequy.bitbull.news.components.ArticleCard

@Composable
fun NewsScreen(newsViewModel: NewsViewModel, navigateToDetails: (Article) -> Unit, searchQuery: String) {
    LaunchedEffect(key1 = Unit) {
        newsViewModel.getData(searchQuery)
    }

    val newsResult = newsViewModel.newsResult.observeAsState()

    when(val result = newsResult.value) {
        is NetworkResponse.Error -> {
            Text(text = result.message)
        }
        NetworkResponse.Loading -> {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        is NetworkResponse.Success -> {
            ArticlesList(
                modifier = Modifier.padding(horizontal = MediumPadding1),
                articles = result.data.articles,
                onClick = navigateToDetails
            )
        }
        null -> {}
    }
}

@Composable
fun ArticlesList(
    modifier: Modifier = Modifier,
    articles: MutableList<Article>,
    onClick: (Article) -> Unit
) {

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(MediumPadding1),
        contentPadding = PaddingValues(all = ExtraSmallPadding2)
    ) {
        items(
            count = articles.size,
        ) {
            articles[it].let { article ->
                ArticleCard(article = article, onClick = {onClick(article)})
            }
        }
    }
}