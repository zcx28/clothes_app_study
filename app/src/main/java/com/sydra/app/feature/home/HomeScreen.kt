package com.sydra.app.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sydra.app.R

@Composable
fun HomeScreen(
    onExploreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.home_background),
            contentDescription = "SYDRA 深色户外主题服装",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Image(
            painter = painterResource(R.drawable.sydra_wordmark),
            contentDescription = "SYDRA",
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-36).dp)
                .width(210.dp)
                .aspectRatio(373f / 35f),
            colorFilter = ColorFilter.tint(Color.White)
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, end = 20.dp, bottom = 68.dp)
        ) {
            Text(
                text = "THEME TITLE",
                color = Color.White,
                fontFamily = FontFamily.SansSerif,
                fontSize = 30.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 3.sp
            )
            Text(
                text = "CONTENT DETAIL DESCRIPTION",
                modifier = Modifier.padding(top = 6.dp),
                color = Color.White,
                fontFamily = FontFamily.SansSerif,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.5.sp
            )
            Box(
                modifier = Modifier
                    .padding(top = 14.dp)
                    .width(120.dp)
                    .height(48.dp)
                    .border(width = 1.dp, color = Color.White.copy(alpha = 0.7f))
                    .clickable(
                        role = Role.Button,
                        onClickLabel = "进入选购",
                        onClick = onExploreClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "即刻探索",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 0.5.sp
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 30.dp)
                .height(2.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .width(82.dp)
                    .fillMaxHeight()
                    .background(Color.White)
            )
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color.White.copy(alpha = 0.28f))
            )
        }
    }
}
