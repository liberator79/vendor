package com.vendor.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.vendor.shared.Alpha
import com.vendor.shared.BorderIdle
import com.vendor.shared.FontSize
import com.vendor.shared.Resources
import com.vendor.shared.RobotoCondensedFont
import com.vendor.shared.SurfaceLighter
import com.vendor.shared.TextPrimary
import com.vendor.shared.TextSecondary
import com.vendor.shared.domain.Product
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProductCard(
    product : Product,
    modifier : Modifier  = Modifier,
    onClick : (String) -> Unit
){
    Row (
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(size = 12.dp))
            .border(
                width = 1.dp,
                color = BorderIdle,
                shape = RoundedCornerShape(12.dp)
            )
            .background(SurfaceLighter)
            .clickable{onClick(product.id)}
    ){
        AsyncImage(
            modifier = Modifier
                .width(120.dp)
                .aspectRatio(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = BorderIdle,
                    shape = RoundedCornerShape(12.dp)
                ),
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(product.thumbnail)
                .crossfade(enable = true)
                .build(),
            contentScale = ContentScale.Fit,
            contentDescription = "Product Images",
        )
        Column (
            modifier = Modifier
                .weight(1f)
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ){
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = product.title.uppercase(),
                fontFamily = RobotoCondensedFont(),
                fontSize = FontSize.MEDIUM,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(alpha = Alpha.HALF),
                text = product.description,
                fontSize = FontSize.REGULAR,
                lineHeight = FontSize.REGULAR * 1.3,
                color = TextPrimary,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        modifier = Modifier.size(14.dp),
                        painter = painterResource(Resources.Icon.Weight),
                        contentDescription = "Weight_icon",
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${product.weight} kg",
                        fontSize = FontSize.EXTRA_SMALL,
                        fontWeight = FontWeight.Normal,
                        color = TextPrimary
                    )
                }
                Text(
                    text = "Rs ${product.price}",
                    fontSize = FontSize.EXTRA_REGULAR,
                    fontWeight = FontWeight.Medium,
                    color = TextSecondary
                )
            }
        }
    }
}